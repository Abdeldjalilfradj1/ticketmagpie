package com.ticketmagpie.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ticketmagpie.Concert;
import com.ticketmagpie.Ticket;
import com.ticketmagpie.User;
import com.ticketmagpie.infrastructure.persistence.ConcertRepository;
import com.ticketmagpie.infrastructure.persistence.TicketRepository;
import com.ticketmagpie.infrastructure.persistence.UserRepository;

@Controller
public class MainController {

  @Autowired
  private ConcertRepository concertRepository;

  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private UserRepository userRepository;

  @RequestMapping("/")
  public String index(Model model) {
    model.addAttribute("concerts", concertRepository.getAllConcerts());
    return "index";
  }

  @RequestMapping("/login")
  public String login() {
    return "login";
  }

  @RequestMapping("/ticket")
  public String ticket(@RequestParam Integer id, Model model) {
    Ticket ticket = ticketRepository.get(id);
    model.addAttribute("ticket", ticket);
    return "ticket";
  }

  @RequestMapping("/forgotpassword")
  public String forgotPassword(@RequestParam(required = false) String user) {
    return "forgotpassword";
  }

  @RequestMapping("/concertimage")
  public void concertImage(@RequestParam(required = true) Integer id, HttpServletResponse httpServletResponse)
      throws IOException {
    Concert concert = concertRepository.get(id);
    InputStream imageStream =
        getClass().getClassLoader().getResourceAsStream(getResourceNameForConcertImage(concert.getImageUrl()));
    IOUtils.copy(imageStream, httpServletResponse.getOutputStream());
  }

  @RequestMapping("/passwordemail")
  public String passwordEmail(@RequestParam String user, Model model) {
    User userFromDatabase = userRepository.get(user);
    System.out.println(userFromDatabase);
    model.addAttribute("userFromDatabase", userFromDatabase);
    return "passwordemail";
  }

  private String getResourceNameForConcertImage(String imageUrl) {
    return "static/images/" + imageUrl;
  }
}
