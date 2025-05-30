package projetoSalf.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
<<<<<<< HEAD
=======
import projetoSalf.mvc.util.SingletonDB;
>>>>>>> Geral

@SpringBootApplication
public class AppProjetoSALF {
    public static void main(String[] args) {
        SpringApplication.run(AppProjetoSALF.class, args);
<<<<<<< HEAD
=======

        SingletonDB.conectar();
>>>>>>> Geral
    }
}
