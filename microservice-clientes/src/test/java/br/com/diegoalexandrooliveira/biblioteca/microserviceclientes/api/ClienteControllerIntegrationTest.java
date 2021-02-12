package br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.api;

import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.Cliente;
import br.com.diegoalexandrooliveira.biblioteca.microserviceclientes.dominio.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    @DisplayName("Deve criar um cliente")
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
    }


    @Test
    @DisplayName("Deve inativar um usuário")
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
    }
}
