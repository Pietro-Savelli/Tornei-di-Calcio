package it.uniroma3.torneidicalcio.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHome(Model model) {
        UserDetails userDetails = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()
                && !(auth.getPrincipal() instanceof String)) {
            userDetails = (UserDetails) auth.getPrincipal();
        }
        model.addAttribute("userDetails", userDetails);
        return "index";
    }
    @GetMapping("/admin")
    public String getAdminHome() {
        return "admin/index";
    }
}