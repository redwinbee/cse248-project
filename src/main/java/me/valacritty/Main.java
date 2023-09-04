package me.valacritty;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import me.valacritty.models.Instructor;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // register our custom deserializer
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Instructor.class, new InstructorDeserializer());
        mapper.registerModule(module);

        // deserialize
        try {
            List<Instructor> instructors = mapper.readValue(new File("instructors.json"), new TypeReference<>() {
            });
            for (Instructor ins : instructors) {
                System.out.println(ins.toString());
            }
        } catch (IOException e) {
            System.err.println("[err]: an error occurred while parsing JSON: " + e.getMessage());
        }
    }
}