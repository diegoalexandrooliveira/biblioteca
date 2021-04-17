package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.api;

import br.com.diegoalexandrooliveira.biblioteca.ClienteRecord;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.config.security.Papeis;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.*;
import net.jqwik.spring.JqwikSpringSupport;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@JqwikSpringSupport
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private Consumer<StringDeserializer, KafkaAvroDeserializer> consumer;

    @Value("${kafka.producer.topic}")
    private String topico;

    private List<ConsumerRecord<?, ?>> lerRegistrosKafka() {
        TopicPartition particao = new TopicPartition(topico, 0);
        Long offSetAtual = consumer.beginningOffsets(List.of(particao)).get(particao);
        Long ultimoOffset = consumer.endOffsets(List.of(particao)).get(particao);

        List<ConsumerRecord<?, ?>> registros = new ArrayList<>();

        for (int i = offSetAtual.intValue(); i < ultimoOffset; i++) {
            registros.addAll(consumer.poll(Duration.of(100, ChronoUnit.MILLIS)).records(particao));
        }
        return registros;
    }

    @Test
    @DisplayName("Deve criar um cliente e publicar mensagem no kafka")
    void teste1() throws Exception {

        String json = "{" +
                "\"usuario\":\"novo_usuario@gmail.com\"," +
                "\"nomeCompleto\":\"Novo Usuario\"," +
                "\"cpf\":\"25009169010\"," +
                "\"logradouro\":\"Rua Um\"," +
                "\"numero\": 2," +
                "\"cidade\":\"São Paulo\"," +
                "\"estado\":\"São Paulo\"" +
                "}";

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        Map<?, ?> objeto = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);

        Cliente cliente = clienteRepository.findById(Long.valueOf(objeto.get("id").toString())).orElseThrow();

        assertEquals("novo_usuario@gmail.com", cliente.getUsuario());
        assertEquals("Novo Usuario", cliente.getNomeCompleto());
        assertEquals("25009169010", cliente.getCpf());
        assertEquals("Rua Um", cliente.getLogradouro());
        assertEquals("São Paulo", cliente.getCidade());
        assertEquals("São Paulo", cliente.getEstado());
        assertEquals(2, cliente.getNumero());
        assertTrue(cliente.isHabilitado());

        List<ConsumerRecord<?, ?>> registrosKafka = lerRegistrosKafka();

        assertEquals(1, registrosKafka.size());

        ClienteRecord clienteRecord = new ObjectMapper().readValue(registrosKafka.get(0).value().toString(), ClienteRecord.class);

        assertEquals("novo_usuario@gmail.com", clienteRecord.getUsuario());
        assertEquals("Novo Usuario", clienteRecord.getNomeCompleto());
        assertEquals("25009169010", clienteRecord.getCpf());
        assertEquals("Rua Um", clienteRecord.getLogradouro());
        assertEquals("São Paulo", clienteRecord.getCidade());
        assertEquals("São Paulo", clienteRecord.getEstado());
        assertEquals(2, clienteRecord.getNumero());
        assertTrue(clienteRecord.getHabilitado());
    }

    @Test
    @DisplayName("Não deve criar um cliente com CPF inválido")
    void teste2() throws Exception {

        String json = "{" +
                "\"usuario\":\"novo_usuario@gmail.com\"," +
                "\"nomeCompleto\":\"Novo Usuario\"," +
                "\"cpf\":\"12345678911\"," +
                "\"logradouro\":\"Rua Um\"," +
                "\"numero\": 2," +
                "\"cidade\":\"São Paulo\"," +
                "\"estado\":\"São Paulo\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        List<ConsumerRecord<?, ?>> registrosKafka = lerRegistrosKafka();

        assertEquals(0, registrosKafka.size());
    }

    @Test
    @DisplayName("Não deve criar um cliente sem usuário")
    void teste3() throws Exception {

        String json = "{" +
                "\"usuario\":\"    \"," +
                "\"nomeCompleto\":\"Novo Usuario\"," +
                "\"cpf\":\"25009169010\"," +
                "\"logradouro\":\"Rua Um\"," +
                "\"numero\": 2," +
                "\"cidade\":\"São Paulo\"," +
                "\"estado\":\"São Paulo\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        List<ConsumerRecord<?, ?>> registrosKafka = lerRegistrosKafka();

        assertEquals(0, registrosKafka.size());
    }

    @Test
    @DisplayName("Não deve existir um cliente com o mesmo nome de usuário")
    void teste4() throws Exception {

        String json = "{" +
                "\"usuario\":\"novo_usuario@gmail.com\"," +
                "\"nomeCompleto\":\"Novo Usuario\"," +
                "\"cpf\":\"25009169010\"," +
                "\"logradouro\":\"Rua Um\"," +
                "\"numero\": 2," +
                "\"cidade\":\"São Paulo\"," +
                "\"estado\":\"São Paulo\"" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders
                .post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        mockMvc.perform(MockMvcRequestBuilders
                .post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        List<ConsumerRecord<?, ?>> registrosKafka = lerRegistrosKafka();

        assertEquals(1, registrosKafka.size());

        ClienteRecord clienteRecord = new ObjectMapper().readValue(registrosKafka.get(0).value().toString(), ClienteRecord.class);

        assertEquals("novo_usuario@gmail.com", clienteRecord.getUsuario());
        assertEquals("Novo Usuario", clienteRecord.getNomeCompleto());
        assertEquals("25009169010", clienteRecord.getCpf());
        assertEquals("Rua Um", clienteRecord.getLogradouro());
        assertEquals("São Paulo", clienteRecord.getCidade());
        assertEquals("São Paulo", clienteRecord.getEstado());
        assertEquals(2, clienteRecord.getNumero());
        assertTrue(clienteRecord.getHabilitado());
    }


    @Test
    @DisplayName("Deve inativar um usuário")
    @WithMockUser(roles = {Papeis.ADMIN})
    void teste5() throws Exception {

        String json = "{" +
                "\"usuario\":\"novo_usuario@gmail.com\"," +
                "\"nomeCompleto\":\"Novo Usuario\"," +
                "\"cpf\":\"25009169010\"," +
                "\"logradouro\":\"Rua Um\"," +
                "\"numero\": 2," +
                "\"cidade\":\"São Paulo\"," +
                "\"estado\":\"São Paulo\"" +
                "}";

        String jsonRetornoPost = mockMvc.perform(MockMvcRequestBuilders
                .post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<?, ?> jsonMap = new ObjectMapper().readValue(jsonRetornoPost, Map.class);

        String jsonRespostaInativar = mockMvc.perform(MockMvcRequestBuilders
                .put("/clientes/inativa/" + jsonMap.get("id"))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<?, ?> jsonRetornoInativar = new ObjectMapper().readValue(jsonRespostaInativar, Map.class);

        assertEquals("novo_usuario@gmail.com", jsonRetornoInativar.get("usuario"));
        assertEquals("Novo Usuario", jsonRetornoInativar.get("nomeCompleto"));
        assertEquals("25009169010", jsonRetornoInativar.get("cpf"));
        assertEquals("Rua Um", jsonRetornoInativar.get("logradouro"));
        assertEquals("São Paulo", jsonRetornoInativar.get("cidade"));
        assertEquals("São Paulo", jsonRetornoInativar.get("estado"));
        assertEquals(2, jsonRetornoInativar.get("numero"));
        assertFalse(Boolean.getBoolean(jsonRetornoInativar.get("habilitado").toString()));


        List<ConsumerRecord<?, ?>> registrosKafka = lerRegistrosKafka();

        assertEquals(2, registrosKafka.size());

        long offPrimeiroRegistro = registrosKafka.get(0).offset();

        for (ConsumerRecord<?, ?> registro : registrosKafka) {
            ClienteRecord clienteRecord = new ObjectMapper().readValue(registro.value().toString(), ClienteRecord.class);

            assertEquals("novo_usuario@gmail.com", clienteRecord.getUsuario());
            assertEquals("Novo Usuario", clienteRecord.getNomeCompleto());
            assertEquals("25009169010", clienteRecord.getCpf());
            assertEquals("Rua Um", clienteRecord.getLogradouro());
            assertEquals("São Paulo", clienteRecord.getCidade());
            assertEquals("São Paulo", clienteRecord.getEstado());
            assertEquals(2, clienteRecord.getNumero());
            if (registro.offset() > offPrimeiroRegistro) {
                assertFalse(clienteRecord.getHabilitado());
            } else {
                assertTrue(clienteRecord.getHabilitado());
            }
        }
    }

    @Test
    @DisplayName("Usuário sem papel de ADMIN não pode inativar outro usuário")
    @WithMockUser(roles = {Papeis.USUARIO}, username = "novo_usuario@gmail.com")
    void teste6() throws Exception {

        String json = "{" +
                "\"usuario\":\"usuario_antigo@gmail.com\"," +
                "\"nomeCompleto\":\"Usuario Antigo\"," +
                "\"cpf\":\"25009169010\"," +
                "\"logradouro\":\"Rua Um\"," +
                "\"numero\": 2," +
                "\"cidade\":\"São Paulo\"," +
                "\"estado\":\"São Paulo\"" +
                "}";

        String jsonRetornoPost = mockMvc.perform(MockMvcRequestBuilders
                .post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<?, ?> jsonMap = new ObjectMapper().readValue(jsonRetornoPost, Map.class);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/clientes/inativa/" + jsonMap.get("id"))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());

        List<ConsumerRecord<?, ?>> registrosKafka = lerRegistrosKafka();

        assertEquals(1, registrosKafka.size());

        ClienteRecord clienteRecord = new ObjectMapper().readValue(registrosKafka.get(0).value().toString(), ClienteRecord.class);

        assertEquals("usuario_antigo@gmail.com", clienteRecord.getUsuario());
        assertEquals("Usuario Antigo", clienteRecord.getNomeCompleto());
        assertEquals("25009169010", clienteRecord.getCpf());
        assertEquals("Rua Um", clienteRecord.getLogradouro());
        assertEquals("São Paulo", clienteRecord.getCidade());
        assertEquals("São Paulo", clienteRecord.getEstado());
        assertEquals(2, clienteRecord.getNumero());
        assertTrue(clienteRecord.getHabilitado());
    }

    @Property(tries = 10)
    void teste7(@ForAll @StringLength(min = 1, max = 255) @AlphaChars  String nomeUsuario,
                @ForAll @StringLength(min = 1, max = 255) @AlphaChars  String nomeCompleto,
                @ForAll @StringLength(min = 1, max = 255) @AlphaChars  String logradouro,
                @ForAll @IntRange(min = 1, max = 1000) int numero,
                @ForAll @StringLength(min = 1, max = 11) @AlphaChars  String cidade,
                @ForAll @StringLength(min = 1, max = 11) @AlphaChars  String estado) throws Exception {

        String json = new ObjectMapper().writeValueAsString(
                Map.of("usuario", nomeUsuario,
                "nomeCompleto", nomeCompleto,
                "cpf", "25009169010",
                "logradouro", logradouro,
                "numero", numero,
                "cidade", cidade,
                "estado", estado));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andReturn();

        Map<?, ?> objeto = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), Map.class);

        Cliente cliente = clienteRepository.findById(Long.valueOf(objeto.get("id").toString())).orElseThrow();

        assertEquals(nomeUsuario, cliente.getUsuario());
        assertEquals(nomeCompleto, cliente.getNomeCompleto());
        assertEquals("25009169010", cliente.getCpf());
        assertEquals(logradouro, cliente.getLogradouro());
        assertEquals(cidade, cliente.getCidade());
        assertEquals(estado, cliente.getEstado());
        assertEquals(numero, cliente.getNumero());
        assertTrue(cliente.isHabilitado());

        List<ConsumerRecord<?, ?>> registrosKafka = lerRegistrosKafka();

        assertEquals(1, registrosKafka.size());

        ClienteRecord clienteRecord = new ObjectMapper().readValue(registrosKafka.get(0).value().toString(), ClienteRecord.class);

        assertEquals(nomeUsuario, clienteRecord.getUsuario());
        assertEquals(nomeCompleto, clienteRecord.getNomeCompleto());
        assertEquals("25009169010", clienteRecord.getCpf());
        assertEquals(logradouro, clienteRecord.getLogradouro());
        assertEquals(cidade, clienteRecord.getCidade());
        assertEquals(estado, clienteRecord.getEstado());
        assertEquals(numero, clienteRecord.getNumero());
        assertTrue(clienteRecord.getHabilitado());
    }
}
