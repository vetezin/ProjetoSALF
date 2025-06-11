package projetoSalf.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import projetoSalf.mvc.util.SingletonDB;

@SpringBootApplication
public class AppProjetoSALF {
    public static void main(String[] args) {
        if (!SingletonDB.conectar()) {
            System.err.println("Falha crítica ao conectar com o banco. Finalizando.");
            return;
        }
        SpringApplication.run(AppProjetoSALF.class, args);
    }

}
