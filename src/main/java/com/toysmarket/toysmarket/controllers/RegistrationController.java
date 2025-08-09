package com.toysmarket.toysmarket.controllers;

import com.toysmarket.toysmarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.toysmarket.toysmarket.models.User;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm, BindingResult bindingResult, Model model){

        //ПОЧЕМУ ТО ВСЕ ПРОВЕРКИ ВАЛИДАЦИИ ПРИШЛОСЬ ПИСАТЬ ТУТ. АННОТАЦИИ SIZE И NOTBLANK НЕ РАБОТАЮТ(((((

        // Вручную проверяем длину username
        if (userForm.getUsername() != null && userForm.getUsername().length() < 10) {
            bindingResult.rejectValue("username", "Size.userForm.username", "Не меньше 10 знаков");
        }

        // Проверка, чтобы username не состоял только из пробелов
        if (userForm.getUsername() != null && userForm.getUsername().trim().isEmpty()) {
            bindingResult.rejectValue("username", "NotBlank.userForm.username", "Имя пользователя не может состоять только из пробелов");
        }

        // Вручную проверяем длину password
        if (userForm.getPassword() != null && userForm.getPassword().length() < 8) {
            bindingResult.rejectValue("password", "Size.userForm.password", "Не меньше 8 знаков");
        }

        // Проверка, чтобы password не состоял только из пробелов
        if (userForm.getUsername() != null && userForm.getPassword().trim().isEmpty()) {
            bindingResult.rejectValue("password", "NotBlank.userForm.password", "Пароль не может состоять только из пробелов");
        }

        //Проверка общих ошибок валидации
        if (bindingResult.hasErrors()) {
            // Безопасное извлечение сообщения об ошибке
            FieldError usernameError = bindingResult.getFieldError("username");
            String errorMessage = (usernameError != null) ? usernameError.getDefaultMessage() : null;
            model.addAttribute("userForm", userForm);
            return "registration";
        }

        //Проверка совпадений паролей
        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Passwords do not match");
            model.addAttribute("userForm", userForm); // Передаём данные формы обратно
            return "registration";
        }

        //Проверка существования пользователя (сейчас saveUser пропускает повторения, в будущем делаю через почту)
        if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "User already exists");
            model.addAttribute("userForm", userForm); // Передаём данные формы обратно
            return "registration";
        }

        return "redirect:/market";
    }
}
