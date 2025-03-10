package com.example.seed;

import com.example.model.Category;
import com.example.model.Habit;
import com.example.model.Profile;
import com.example.model.User;
import com.example.repository.CategoryRepository;
import com.example.repository.HabitRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * For testing purpose, this class injects data into our application.
 */
@Component
public class SeedData implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final HabitRepository habitRepository;
    @Autowired
    public SeedData(@Lazy PasswordEncoder passwordEncoder, //loads on-demand
                    UserRepository userRepository,
                    CategoryRepository categoryRepository,
                    HabitRepository habitRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.habitRepository = habitRepository;
    }

    /**
     * Everytime the application starts, this method will be invoked
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setEmailAddress("gabrielleynara@ymail.com");
        user.setPassword(passwordEncoder.encode("gaby1234"));
        user.setProfile(new Profile());

        Category category = new Category();
        category.setName("Morning Routine");
        category.setDescription("Habits related to morning routine");
        category.setUser(user);
        userRepository.save(user);
        categoryRepository.save(category);

        Habit habit = new Habit();
        habit.setName("Shower");
        habit.setTrigger("Wake up");
        habit.setOutcome("Starting the day fresh");
        habit.setRoutine("Every day");
        habit.setCategory(category);
        habit.setUser(user);
        habitRepository.save(habit);

        User user2 = new User();
        user2.setEmailAddress("gabrielle@ymail.com");
        user2.setPassword(passwordEncoder.encode("gaby1234"));
        user2.setProfile(new Profile());
        userRepository.save(user2);

        Category category1 = new Category();
        category1.setName("Bed Time");
        category1.setDescription("Habits related to bed routine");
        category1.setUser(user2);
        categoryRepository.save(category1);

        Habit habit1 = new Habit();
        habit1.setName("Skin Care");
        habit1.setTrigger("Brush teeth");
        habit1.setOutcome("Improved self esteem");
        habit1.setRoutine("Every day");
        habit1.setCategory(category1);
        habit1.setUser(user2);
        habitRepository.save(habit1);
    }

}
