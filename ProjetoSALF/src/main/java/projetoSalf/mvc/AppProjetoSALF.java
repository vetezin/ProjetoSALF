package projetoSalf.mvc;

import projetoSalf.mvc.util.SingletonDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AppProjetoSALF {

    public static void main(String[] args) {
        if(!SingletonDB.conectar()){
            System.out.println("Nao foi possivel conectar");
        }
        SpringApplication.run(AppProjetoSALF.class, args);
    }

}
