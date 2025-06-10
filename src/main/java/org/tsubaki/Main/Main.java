package org.tsubaki.Main;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.tsubaki.DataBase.Mybatis.Service.UserService;
import org.tsubaki.Game.Object.GameObjectTypeScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.tsubaki"})
@MapperScan(basePackages = {"org.tsubaki"})
@GameObjectTypeScan(basePackages = {"org.tsubaki"})
public class Main {

    public static void main(String[] args) throws Exception {





        SpringApplication.run(Main.class);
    }


}

