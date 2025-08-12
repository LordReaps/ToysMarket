package com.toysmarket.toysmarket.controllers;

import com.toysmarket.toysmarket.models.Post_cards;
import com.toysmarket.toysmarket.models.User;
import com.toysmarket.toysmarket.models.User_Post_Card;
import com.toysmarket.toysmarket.repo.Post_cards_repository;
import com.toysmarket.toysmarket.repo.User_Post_Card_repository;
import com.toysmarket.toysmarket.repo.User_repository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
public class MainController {

    private void setCurrentUserAttributes(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()){
            Object principal = auth.getPrincipal();

            if (!(principal instanceof String && "anonymousUser".equals(principal))){
                if (principal instanceof User currentUser){
                    model.addAttribute("currentUser", currentUser);
                    model.addAttribute("userId", currentUser.getId());
                }
                else{
                    String username = principal.toString();
                    model.addAttribute("currentUser", new java.util.HashMap<String, Object>() {{
                        put("username", username);
                    }});
                }
            }
        }
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @Autowired
    private Post_cards_repository post_cards_repository;

    @Autowired
    private User_repository user_repository;

    @Autowired
    private User_Post_Card_repository user_post_card_repository;

    @GetMapping("/market")
    public String market(Model model, HttpServletRequest request) {

        //Передача в модель текущего пользователя
        setCurrentUserAttributes(model);

        //Получение всех карточек
        Iterable<Post_cards> post_cards = post_cards_repository.findAll();
        model.addAttribute("cards", post_cards);

        // Построение карты авторов по id карточки
        Map<Integer, User> authorUsers = new HashMap<>();
        for (Post_cards c : post_cards) {
            User author = c.getAuthor();
            if (author != null) {
                authorUsers.put(c.getId(), author);
            }
        }
        model.addAttribute("authorUsers", authorUsers);

        // Добавление текущего URI как currentUrl
        model.addAttribute("currentUrl", request.getRequestURI());

        return "market";
    }

    @GetMapping("/guest")
    public String guest(Model model, HttpServletRequest request, HttpSession session) {
        String targetUrl = request.getParameter("targetUrl");
        if (targetUrl == null || targetUrl.isEmpty()) {
            targetUrl = request.getRequestURI();
        }
        session.setAttribute("targetUrl", targetUrl);

        setCurrentUserAttributes(model);

        return "guest";
    }


    @GetMapping("/card/{id}")
    public String card(@PathVariable("id") Integer id, Model model) {

        //Передача в модель текущего пользователя
        setCurrentUserAttributes(model);

        //Получение карточки
        Post_cards post_cards = post_cards_repository.findById(id).orElse(null);
        if (post_cards == null) {
            return "redirect:/market";
        }

        // Автор теперь доступен через связь
        User authorUser = post_cards.getAuthor();
        model.addAttribute("authorUser", authorUser);

        model.addAttribute("card", post_cards);
        return "card";
    }

    @GetMapping("/user/{id}")
    public String user(@PathVariable("id") Integer id, Model model) {

        //Передача в модель текущего пользователя
        setCurrentUserAttributes(model);

        // Получение пользователя по ID

        // Получение карточек пользователя через репозиторий (избегаем LazyInitializationException)
        Set<User_Post_Card> userCards = user_post_card_repository.findAllByUserId(id);
        model.addAttribute("userCards", userCards);

        return "user";
    }

}
