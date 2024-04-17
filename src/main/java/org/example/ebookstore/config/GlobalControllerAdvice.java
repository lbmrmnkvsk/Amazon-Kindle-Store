package org.example.ebookstore.config;

import jakarta.servlet.http.HttpServletRequest;
import org.example.ebookstore.entities.Currency;
import org.example.ebookstore.entities.dtos.CategoryDto;
import org.example.ebookstore.entities.dtos.UserDto;
import org.example.ebookstore.services.interfaces.CategoryService;
import org.example.ebookstore.services.interfaces.CurrencyService;
import org.example.ebookstore.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {
    private final CurrencyService currencyService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public GlobalControllerAdvice(CurrencyService currencyService, UserService userService, CategoryService categoryService) {
        this.currencyService = currencyService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    //TODO: cache this for one day
    @ModelAttribute("allCurrencies")
    public List<Currency> populateCurrencies() {
        return currencyService.getAllCurrencies();
    }

    @ModelAttribute("selectedCurrency")
    public Currency populateSelectedCurrency(HttpServletRequest request) {
        return this.userService.getSelectedCurrency(request);
    }

    @ModelAttribute("isLoggedIn")
    public Boolean addIsLoggedIn(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof AnonymousAuthenticationToken);
    }

    @ModelAttribute("userDto")
    public UserDto addUserDto(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String)) {
            String username = ((UserDetails) authentication.getPrincipal()).getUsername();
            Optional<UserDto> optional = this.userService.getUserDtoByUsername(username);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
    }

    //TODO: cache this for one day
    @ModelAttribute("level2Categories")
    public List<CategoryDto> populateCategories() {
        return this.categoryService.getDirectSubcategories(1L);
    }

    //TODO: fix this
    @ModelAttribute("currentUrl")
    public String populateCurrentUrl(HttpServletRequest request) {
        String url = request.getRequestURL().toString();
        if (url.contains("change-currency")) {
            return url.substring(0, url.indexOf("change-currency"));
        } else {
            return url;
        }
    }

    //TODO: check
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }
}
