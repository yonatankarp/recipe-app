package com.yonatankarp.recipeapp.bootstrap;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import com.yonatankarp.recipeapp.exceptions.CategoryNotFoundException;
import com.yonatankarp.recipeapp.exceptions.UnitOfMeasureNotFoundException;
import com.yonatankarp.recipeapp.model.Category;
import com.yonatankarp.recipeapp.model.Difficulty;
import com.yonatankarp.recipeapp.model.Ingredient;
import com.yonatankarp.recipeapp.model.Notes;
import com.yonatankarp.recipeapp.model.Recipe;
import com.yonatankarp.recipeapp.model.UnitOfMeasure;
import com.yonatankarp.recipeapp.repositories.CategoryRepository;
import com.yonatankarp.recipeapp.repositories.RecipeRepository;
import com.yonatankarp.recipeapp.repositories.UnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RecipeBootstrap implements CommandLineRunner {

    private static final String UOM_EACH = "each";
    private static final String UOM_TABLESPOON = "tablespoon";
    private static final String UOM_TEASPOON = "teaspoon";
    private static final String UOM_DASH = "dash";
    private static final String UOM_PINT = "pint";
    private static final String UOM_CUP = "cup";
    private static final String CATEGORY_AMERICAN = "american";
    private static final String CATEGORY_MEXICAN = "mexican";

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    private final Map<String, UnitOfMeasure> unitOfMeasures = new HashMap<>();
    private final Map<String, Category> categories = new HashMap<>();

    private final ClassLoader classLoader = RecipeBootstrap.class.getClassLoader();


    @Transactional
    @Override
    public void run(String... args) {
        recipeRepository.saveAll(getRecipes());
    }

    private List<Recipe> getRecipes() {
        loadUnitOfMeasures();
        loadCategories();

        final List<Recipe> recipes = new ArrayList<>(2);
        recipes.add(getGuacamole());
        recipes.add(getTacos());
        return recipes;
    }

    private void loadUnitOfMeasures() {
        // Get UOMs
        final var eachUomOptional = unitOfMeasureRepository.findByDescription("Each");
        if (eachUomOptional.isEmpty()) {
            throw new UnitOfMeasureNotFoundException("Expected UOM 'Each' Not Found");
        }

        final var tableSpoonUomOptional = unitOfMeasureRepository.findByDescription("Tablespoon");
        if (tableSpoonUomOptional.isEmpty()) {
            throw new UnitOfMeasureNotFoundException("Expected UOM 'Tablespoon' Not Found");
        }

        final var teaSpoonUomOptional = unitOfMeasureRepository.findByDescription("Teaspoon");
        if (teaSpoonUomOptional.isEmpty()) {
            throw new UnitOfMeasureNotFoundException("Expected UOM 'Teaspoon' Not Found");
        }

        final var dashUomOptional = unitOfMeasureRepository.findByDescription("Dash");
        if (dashUomOptional.isEmpty()) {
            throw new UnitOfMeasureNotFoundException("Expected UOM 'Dash' Not Found");
        }

        final var pintUomOptional = unitOfMeasureRepository.findByDescription("Pint");
        if (pintUomOptional.isEmpty()) {
            throw new UnitOfMeasureNotFoundException("Expected UOM 'Pint' Not Found");
        }

        final var cupsUomOptional = unitOfMeasureRepository.findByDescription("Cup");
        if (cupsUomOptional.isEmpty()) {
            throw new UnitOfMeasureNotFoundException("Expected UOM 'Cup' Not Found");
        }

        unitOfMeasures.put(UOM_EACH, eachUomOptional.get());
        unitOfMeasures.put(UOM_TABLESPOON, tableSpoonUomOptional.get());
        unitOfMeasures.put(UOM_TEASPOON, teaSpoonUomOptional.get());
        unitOfMeasures.put(UOM_DASH, dashUomOptional.get());
        unitOfMeasures.put(UOM_PINT, pintUomOptional.get());
        unitOfMeasures.put(UOM_CUP, cupsUomOptional.get());
    }

    private void loadCategories() {
        final var americanCategoryOptional = categoryRepository.findByDescription("American");
        if (americanCategoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("Expected Category 'American' Not Found");
        }

        final var mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");
        if (mexicanCategoryOptional.isEmpty()) {
            throw new CategoryNotFoundException("Expected Category 'Mexican' Not Found");
        }

        categories.put(CATEGORY_AMERICAN, americanCategoryOptional.get());
        categories.put(CATEGORY_MEXICAN, mexicanCategoryOptional.get());
    }

    private Recipe getGuacamole() {
        final var eachUom = unitOfMeasures.get(UOM_EACH);
        final var tableSpoonUom = unitOfMeasures.get(UOM_TABLESPOON);
        final var teaspoonUom = unitOfMeasures.get(UOM_TEASPOON);
        final var dashUom = unitOfMeasures.get(UOM_DASH);

        final var americanCategory = categories.get(CATEGORY_AMERICAN);
        final var mexicanCategory = categories.get(CATEGORY_MEXICAN);

        final var image = loadImage("guacamole400x400.jpeg");

        // Perfect Guacamole
        final var guacamoleNotes = Notes.builder()
                .recipeNotes("For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                        "Feel free to experiment! One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). Try guacamole with added pineapple, mango, or strawberries.\n" +
                        "The simplest version of guacamole is just mashed avocados with salt. Don't let the lack of availability of other ingredients stop you from making guacamole.\n" +
                        "To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.\n" +
                        "\n" +
                        "\n" +
                        "Read more: https://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvoun5ws")
                .build();

        final var recipeIngredients = new HashSet<Ingredient>();
        recipeIngredients.add(createIngredient("ripe avocados", "2", eachUom));
        recipeIngredients.add(createIngredient("Kosher salt", ".5", teaspoonUom));
        recipeIngredients.add(createIngredient("fresh lime juice or lemon juice", "2", tableSpoonUom));
        recipeIngredients.add(createIngredient("minced red onion or thinly sliced green onion", "2", tableSpoonUom));
        recipeIngredients.add(createIngredient("serrano chiles, stems and seeds removed, minced", "2", eachUom));
        recipeIngredients.add(createIngredient("Cilantro", "2", tableSpoonUom));
        recipeIngredients.add(createIngredient("freshly grated black pepper", "2", dashUom));
        recipeIngredients.add(createIngredient("ripe tomato, seeds and pulp removed, chopped", ".5", eachUom));

        final var recipeCategories = new HashSet<Category>();
        recipeCategories.add(americanCategory);
        recipeCategories.add(mexicanCategory);

        final var guacamole = Recipe.builder()
                .description("Perfect Guacamole")
                .prepTime(10)
                .cookTime(0)
                .servings(4)
                .difficulty(Difficulty.EASY)
                .directions("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon" +
                        "\n" +
                        "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)" +
                        "\n" +
                        "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                        "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                        "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                        "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                        "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                        "\n" +
                        "\n" +
                        "Read more: https://www.simplyrecipes.com/recipes/perfect_guacamole/#ixzz4jvpiV9Sd")
                .notes(guacamoleNotes)
                .ingredients(recipeIngredients)
                .categories(recipeCategories)
                .url("https://www.simplyrecipes.com/recipes/perfect_guacamole/")
                .source("Simply Recipes")
                .image(image)
                .build();

        guacamoleNotes.setRecipe(guacamole);
        recipeIngredients.forEach(ingredient -> ingredient.setRecipe(guacamole));
        recipeCategories.forEach(category -> category.getRecipes().add(guacamole));

        return guacamole;
    }

    private Recipe getTacos() {

        final var eachUom = unitOfMeasures.get(UOM_EACH);
        final var tableSpoonUom = unitOfMeasures.get(UOM_TABLESPOON);
        final var teaspoonUom = unitOfMeasures.get(UOM_TEASPOON);
        final var pintUom = unitOfMeasures.get(UOM_PINT);
        final var cupsUom = unitOfMeasures.get(UOM_CUP);

        final var americanCategory = categories.get(CATEGORY_AMERICAN);
        final var mexicanCategory = categories.get(CATEGORY_MEXICAN);

        final var image = loadImage("tacos400x400.jpeg");

        // Spicy Grilled Chicken Tacos
        final var tacoNotes = Notes.builder()
                .recipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                        "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                        "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                        "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                        "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                        "\n" +
                        "Read more: https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvu7Q0MJ")
                .build();

        final var recipeIngredients = new HashSet<Ingredient>();
        recipeIngredients.add(createIngredient("Ancho Chili Powder", "2", tableSpoonUom));

        recipeIngredients.add(createIngredient("Ancho Chili Powder", "2", tableSpoonUom));
        recipeIngredients.add(createIngredient("Dried Oregano", "1", teaspoonUom));
        recipeIngredients.add(createIngredient("Dried Cumin", "1", teaspoonUom));
        recipeIngredients.add(createIngredient("Sugar", "1", teaspoonUom));
        recipeIngredients.add(createIngredient("Salt", ".5", teaspoonUom));
        recipeIngredients.add(createIngredient("Clove of Garlic, Choppedr", "1", eachUom));
        recipeIngredients.add(createIngredient("finely grated orange zestr", "1", tableSpoonUom));
        recipeIngredients.add(createIngredient("fresh-squeezed orange juice", "3", tableSpoonUom));
        recipeIngredients.add(createIngredient("Olive Oil", "2", tableSpoonUom));
        recipeIngredients.add(createIngredient("boneless chicken thighs", "4", tableSpoonUom));
        recipeIngredients.add(createIngredient("small corn tortillasr", "8", eachUom));
        recipeIngredients.add(createIngredient("packed baby arugula", "3", cupsUom));
        recipeIngredients.add(createIngredient("medium ripe avocados, slic", "2", eachUom));
        recipeIngredients.add(createIngredient("radishes, thinly sliced", "4", eachUom));
        recipeIngredients.add(createIngredient("cherry tomatoes, halved", ".5", pintUom));
        recipeIngredients.add(createIngredient("red onion, thinly sliced", ".25", eachUom));
        recipeIngredients.add(createIngredient("Roughly chopped cilantro", "4", eachUom));
        recipeIngredients.add(createIngredient("cup sour cream thinned with 1/4 cup milk", "4", cupsUom));
        recipeIngredients.add(createIngredient("lime, cut into wedges", "4", eachUom));

        final var recipeCategories = new HashSet<Category>();
        recipeCategories.add(americanCategory);
        recipeCategories.add(mexicanCategory);

        final var tacos = Recipe.builder()
                .description("Spicy Grilled Chicken Taco")
                .cookTime(9)
                .prepTime(20)
                .servings(4)
                .difficulty(Difficulty.MODERATE)
                .directions("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                        "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                        "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                        "\n" +
                        "\n" +
                        "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                        "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                        "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                        "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.\n" +
                        "\n" +
                        "\n" +
                        "Read more: https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/#ixzz4jvtrAnNm")
                .notes(tacoNotes)
                .url("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/")
                .source("Simply Recipes")
                .image(image)
                .ingredients(recipeIngredients)
                .categories(recipeCategories)
                .build();

        tacoNotes.setRecipe(tacos);
        recipeIngredients.forEach(ingredient -> ingredient.setRecipe(tacos));
        recipeCategories.forEach(category -> category.getRecipes().add(tacos));

        return tacos;
    }

    private Ingredient createIngredient(final String description,
                                        final String amount,
                                        final UnitOfMeasure uom) {
        return new Ingredient(description, new BigDecimal(amount), uom);
    }

    @SneakyThrows
    private byte[] loadImage(final String imageName) {
        return classLoader.getResourceAsStream("static/images/" + imageName).readAllBytes();
    }
}
