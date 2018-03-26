package com.lgiulian.bakingapp;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;

        import com.lgiulian.bakingapp.model.Recipe;

        import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Recipe> recipes = Recipe.getAllRecipes(this);
//        for (Recipe recipe: recipes) {
//            Log.d(TAG, recipe.toString());
//        }
    }
}
