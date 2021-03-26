package br.com.diegoalexandrooliveira.biblioteca.microserviceemprestimos.clientes.api;

import org.springframework.stereotype.Service;

@Service
public class Servico {


    public void enviar() {
        try {
            Thread.sleep(150);
            System.out.println(System.currentTimeMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
