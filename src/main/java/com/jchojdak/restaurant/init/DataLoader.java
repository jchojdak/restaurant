package com.jchojdak.restaurant.init;

import com.jchojdak.restaurant.model.Category;
import com.jchojdak.restaurant.model.Product;
import com.jchojdak.restaurant.model.Role;
import com.jchojdak.restaurant.model.User;
import com.jchojdak.restaurant.repository.CategoryRepository;
import com.jchojdak.restaurant.repository.ProductRepository;
import com.jchojdak.restaurant.repository.RoleRepository;
import com.jchojdak.restaurant.repository.UserRepository;
import com.jchojdak.restaurant.service.IRoleService;
import com.jchojdak.restaurant.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final IRoleService roleService;

    private final IUserService userService;

    private final Environment environment;

    @Override
    public void run(String... args) {
        if (Arrays.asList(environment.getActiveProfiles()).contains("loadData")) {
            Role role1 = new Role();
            role1.setName("ROLE_USER");
            roleRepository.save(role1);

            Role role2 = new Role();
            role2.setName("ROLE_ADMIN");
            roleRepository.save(role2);

            Role adminRole = roleService.findByName("ROLE_ADMIN");

            User admin = new User(
                    1L,
                    "Admin",
                    "Admin",
                    "admin@admin",
                    "admin",
                    "123123123",
                    "XX-XXX",
                    "City",
                    "Address line 1",
                    "Address line 2",
                    Collections.singleton(adminRole)
                    );
            userService.registerUser(admin);

            admin.setRoles(Collections.singletonList(adminRole));
            userRepository.save(admin);

            /*User user = new User();
            user.setFirstName("admin");
            user.setEmail("admin@admin.pl");
            user.setPassword("admin");
            System.out.println("PASSWORD: " + user.getPassword());
            user.setRoles(Collections.singletonList(role2));

            userService.registerUser(user);*/


            Category pizzaCategory = new Category();
            pizzaCategory.setName("Pizza");
            categoryRepository.save(pizzaCategory);

            Category dessertsCategory = new Category();
            dessertsCategory.setName("Desery");
            categoryRepository.save(dessertsCategory);

            Category beersCategory = new Category();
            beersCategory.setName("Piwa");
            categoryRepository.save(beersCategory);

            Category alcoholicDrinksCategory = new Category();
            alcoholicDrinksCategory.setName("Drinki alkoholowe");
            categoryRepository.save(alcoholicDrinksCategory);

            Arrays.asList(
                    new Product(
                            null,
                            "Pizza Pepperoni",
                            "10-20",
                            new BigDecimal(30),
                            true,
                            "Pizzas/pizza-1.jpg",
                            "Sos pomidorowy, mozarella, pepperoni",
                            "Pizza, która z pewnością posmakuje każdemu!",
                            pizzaCategory
                    ),
                    new Product(
                            null,
                            "Sernik",
                            "5-10",
                            new BigDecimal(15),
                            false,
                            "Desserts/dessert-1.jpg",
                            "Mailny, borówki, truskawki, kawałki czekolady",
                            "Delikatny sernik z dodatkiem świeżych owoców i kawałków czekolady deserowej",
                            dessertsCategory
                    ),
                    new Product(
                            null,
                            "Piwo jasne",
                            "3-5",
                            new BigDecimal(10),
                            true,
                            "Beers/beer-1.jpg",
                            "",
                            "Nasz autorskie, rzemieślnicze piwo jasne",
                            alcoholicDrinksCategory
                    ),
                    new Product(
                            null,
                            "Piwo ciemne",
                            "3-5",
                            new BigDecimal(10),
                            false,
                            "Beers/beer-2.jpg",
                            "",
                            "Nasz autorskie, rzemieślnicze ciemne piwo",
                            alcoholicDrinksCategory
                    ),
                    new Product(
                            null,
                            "Whiskey sour",
                            "5-7",
                            new BigDecimal(20),
                            true,
                            "Alcdrinks/alcdrink-1.jpg",
                            "",
                            "Klasyk wśród drinków! Idealne połączenie lekkiej goryczy, kwaśnych cytrusów ze słodyczą, a do tego delikatna pianka z białka",
                            alcoholicDrinksCategory
                    ),
                    new Product(
                            null,
                            "Pizza hawajska",
                            "10-20",
                            new BigDecimal(30),
                            false,
                            "Pizzas/pizza-2.jpg",
                            "Sos pomidorowy, mozarella, szynka, ananas",
                            "Niekażdy ją lubi, ale to idealne połączenie słodkiego i słonego smaku. Spróbuj!",
                            pizzaCategory
                    ),
                    new Product(
                            null,
                            "Pizza tropikalna",
                            "10-20",
                            new BigDecimal(30),
                            true,
                            "Pizzas/pizza-3.jpg",
                            "Sos barbecue, mozarella, cebula, boczek, kurczak, ananas",
                            "Ulepszona wersja pizzy hawajskiej z naprawdę sporą ilością mięsa",
                            pizzaCategory
                    )
            ).forEach(productRepository::save);
        }
    }
}
