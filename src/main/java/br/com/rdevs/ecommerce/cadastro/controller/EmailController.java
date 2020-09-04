package br.com.rdevs.ecommerce.cadastro.controller;

import br.com.rdevs.ecommerce.cadastro.model.entity.TbCliente;
import br.com.rdevs.ecommerce.cadastro.repository.CadastroRepository;
import br.com.rdevs.ecommerce.cadastro.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
public class EmailController{

    @Autowired
    private EmailService service;

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private CadastroController cadastroController;

    @Autowired
    private JavaMailSender mailSender;

//    @RequestMapping(path = "/email-send", method = RequestMethod.GET)
//    public String sendMail() {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setText("Este é o meu teste de envio de email");
//        message.setTo("andremouraer@gmail.com");
//        message.setFrom("andremouraer@gmail.com");
//
//        try {
//            mailSender.send(message);
//            return "Email enviado com sucesso!";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Erro ao enviar email.";
//        }
//    }

    @PutMapping(value = "/esqueciSenha")
    public String trocarSenha(String login){
        String email = service.esqueciSenha(login);
        TbCliente cliente = cadastroRepository.findByDsEmail(login);
        String[] carct ={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j",
                "k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B",
                "C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

        String senha="";

        for (int x=0; x<10; x++){
            int j = (int) (Math.random()*carct.length);
            senha += carct[j];
        }

        cliente.setPwCliente(Base64.getEncoder().encodeToString(senha.getBytes()));
        cadastroRepository.save(cliente);



        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("Essa é uma mensagem automatica favor não responder!!");
        message.setText("Foi solicitado a alteração da senha, sua nova senha é: "+senha);
        message.setSubject("Alteração de senha:");
        message.setTo(email);
        message.setFrom("ecommerceraiadrogasil1@gmail.com");

        try {
            mailSender.send(message);
            return "Email enviado com sucesso!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao enviar email.";
        }

    }
}