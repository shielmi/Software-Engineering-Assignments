package com.example.michaelshiel.databaseaddition;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;

public class homePage extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mSnackDatabaseReference;
    private DatabaseReference mBreakfastDatabaseReference;
    private DatabaseReference mLunchReference;
    private DatabaseReference mDinnerReference;
    private DatabaseReference mTescoPricesReference;
    private DatabaseReference mSupervaluPricesReference;
    private DatabaseReference mAldiReference;
    private DatabaseReference mUserReference;
    private DatabaseReference mGroceryListReference;

    //private ListView mRecipeListView;
    private static final int RC_SIGN_IN = 1;

    String name;
    String mPrepTime;
    String mCookTime;
    String mCalories;
    String mProtein;
    String mCarbs;
    String mFat;
    String mMeasurement;
    String mSurvingSuggestion;
    String mPrice;
    String mCurrency;
    String mWeight;
    Map<String, String> mAmount;
    Map<String, String> mMesurement;
    Map<String, Map<String,String>> mIngredients;
    Map<String, Map<String,String>> mIngredientsPush;
    Map<String,String> mInstructions;
    Map<String, String> mMeasure;

    String mWriter;
    String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mSnackDatabaseReference = mFirebaseDatabase.getReference().child("snacks");
        mBreakfastDatabaseReference = mFirebaseDatabase.getReference().child("breakfast");
        mLunchReference = mFirebaseDatabase.getReference().child("lunch");
        mDinnerReference = mFirebaseDatabase.getReference().child("dinner");
        mTescoPricesReference = mFirebaseDatabase.getReference().child("tescoPrices");
        mSupervaluPricesReference = mFirebaseDatabase.getReference().child("supervaluPrices");
        mAldiReference = mFirebaseDatabase.getReference().child("aldiPrices");
        mUserReference = mFirebaseDatabase.getReference().child("user");
        mGroceryListReference = mFirebaseDatabase.getReference().child("groceryList");
        mInstructions= new LinkedHashMap<>();
        new doit().execute();
      //  new doit2().execute();
    }


    public class doit extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                String url = "https://www.muscleandstrength.com/recipes/mini-chocolate-coconut-protein-cookies";
                Document document = Jsoup.connect(url).get();

                getRecipe(document, url);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Recipe theRecipe = new Recipe(name, mCalories, mPrepTime, mCookTime, mProtein, mCarbs, mFat, mMeasurement,
                    mIngredients, mInstructions, mSurvingSuggestion, mWriter, mUrl, mSnackDatabaseReference);
            mBreakfastDatabaseReference.push().setValue(theRecipe);


//            getIngredients(document, url, mBreakfastDatabaseReference);


            //  TescoPrice price = new TescoPrice(name,mAmount,mCurrency, mPrice);
            //  mTescoPricesReference.push().setValue(price);

        }


        public void getRecipe(Document document, String url) throws IOException {

            name = document.title();

//            Elements prepTime = document.getElementsByClass("recipe-time-bar left").first().getElementsByClass("info");
//            mPrepTime = prepTime.text();
//
//            Elements cookTime = document.getElementsByClass("recipe-time-bar right").first().getElementsByClass("info");
//            mCookTime = cookTime.text();
            mPrepTime = null;
            mCookTime = null;

            Elements calories = document.getElementsByClass("field field-name-field-recipe-calories field-type-text field-label-hidden");
            mCalories = calories.text();


            Elements protein = document.select("[itemprop=proteinContent]");
            Element measure = document.getElementsByClass("amount").first();
            mProtein = protein.text();
            mMeasurement = measure.text();

            Elements carbs = document.select("[itemprop=carbohydrateContent]");
            mCarbs = carbs.text();


            Elements fat = document.select("[itemprop=fatContent]");
            mFat = fat.text();

            //When amounts were at the end
//            Elements ingredients = document.getElementsByClass("recipe-check-list").first().getElementsByTag("li");
//            for (Element el : ingredients) {
//               List<String> singleRecipe = new ArrayList<>();
//                Element recipe3 = el.select("[itemprop=recipeIngredient]").first();
//                //mRecipe.add(recipe3.text());
//                String test = recipe3.text();
//                Scanner scan = new Scanner(test);
//                scan.useDelimiter(", ");
//                while(scan.hasNext())
//                {
//                    String s = scan.next();
//                    if (s.matches(".*\\d+.*"))
//                    {
//                        String st = extractNumber(s);
//                        String amount = extractServing(s);
//                        singleRecipe.add(st);
//                        singleRecipe.add(amount);
//                }
//                else
//                {
//                    singleRecipe.add(s);
//                }
//            }
//            mIngredients.add(singleRecipe);
//
//            }


            mIngredients = new HashMap<>();

            List<Map> x = new ArrayList<>();
            //When amounts were at the start Possibly make own function to add ingredients after recipe is added
            Elements ingredients = document.getElementsByClass("recipe-check-list").first().getElementsByTag("li");

            for (Element el : ingredients) {

                Map<String, String> amount = new HashMap<>();

                Element theIngredient = el.select("[itemprop=recipeIngredient]").first();
                String test = theIngredient.text();
                Scanner scan = new Scanner(test);
                //scan.useDelimiter(", ");
                int i = 0;
                String rest = "";
                if (scan.hasNext() && i == 0) {
                    String s = scan.next();
                    if (s.matches(".*\\d+.*") || s.contains("½") || s.contains("¼") || s.contains("⅓") || s.contains("¾")) {
                        String st = extractNumber(s);
                        String temp = scan.next();
                        if (temp.equals("scoops") || temp.equals("scoop") || temp.equals("ounces") || temp.equals("tbsp") || temp.equals("cups") ||
                                temp.equals("cup") || temp.equals("ounce") || temp.equals("tsps") || temp.equals("tsp") || temp.equals("oz")
                                || temp.equals("tbs") || temp.equals("tablespoon") || temp.equals("grams") || temp.equals("tbsp.")
                                || temp.equals("slices") || temp.equals("lbs") || temp.equals("packet") || temp.equals("Cans")
                                || temp.equals("lb") || temp.equals("batch") || temp.equals("bag") || temp.equals("tsp.") || temp.equals("pinch")) {
                            String measurement = extractServing(temp);
                            amount.put("Amount", st);
                            amount.put("Measure", measurement);

                        } else {
                            rest = rest.concat(temp);
                            amount.put("Amount", st);
                            amount.put("Measure", "NULL");
                        }

                    } else {
                        rest = rest.concat(s);
                        amount.put("Amount", "NULL");
                        amount.put("Measure", "NULL");
                    }
                }

                while (scan.hasNext()) {
                    String temp = scan.next();
                    if (rest.equals("") == false) {
                        rest = rest.concat(" " + temp);
                    } else if (temp.equals("of") == false) {
                        rest = rest.concat(temp);
                    }

                }

                mIngredients.put(rest, amount);
               // mIngredients.add(mIngredientsPush);
            }


//            //Bullet Points
//            Elements instructions = document.select("[itemprop=recipeInstructions]").first().getElementsByTag("li");
//            for (Element el : instructions)
//            {
//                Element recipe4 = el.getElementsByTag("li").first();
//                mInstructions.add(recipe4.text());
//            }

//            //Unordered List
//            Elements instructions = document.select("[itemprop=recipeInstructions]").first().getElementsByTag("p");
//            for (Element el : instructions)
//            {
//                Element recipe4 = el.getElementsByTag("p").first();
//                mInstructions.add(recipe4.text());
//            }

            //Ordered List
            Elements instructions = document.getElementsByTag("ol").first().getElementsByTag("li");
            int i = 0;
            for (Element el : instructions) {

                Element recipe4 = el.getElementsByTag("li").first();
                mInstructions.put("instruction" + i,recipe4.text());
                i++;
            }
//
//                Map<String,String> emptyMap1 = new HashMap<>();
//                emptyMap1.put("b","");
//                mInstructions.put("b","");
//                mIngredients.put("Skim milk", null);

                Elements yield = document.select("[itemprop=recipeYield]").first().getElementsByTag("p");
                mSurvingSuggestion = yield.text();

                Elements author = document.getElementsByClass("aboutAuthor").first().getElementsByClass("name");
                mWriter = author.text();

                mUrl = url;

        }

        public void TescoPrices(Document document, String url){
            //mAmount = new ArrayList<>();
            Elements info = document.getElementsByClass("productLists").first().getElementsByTag("li");
            int i = 0;
            for (Element el : info) {
                if (el.getElementsByClass("linePrice").first() != null)
                {
                    mAmount = new HashMap<>();
                    Element names = el.getElementsByTag("a").first();
                    name = names.text();
                    int amount = 0;
                    if(name.contains("Each")||name.contains("Loose"))
                    {
                        amount =1;
                    }
                    else if (name.matches(".*\\d+.*"))
                    {
                        String st = extractNumber(name);
                        amount = Integer.parseInt(st);
                        //int last = 0;
                        if(name.contains("Pack"))
                        {
                            Scanner scan = new Scanner(name);
                            while(scan.hasNext())
                            {
                                //System.out.print(scan.next());
                                if(scan.next().equals("Pack")&&scan.hasNext() ==false)
                                {
                                    mWeight=null;
                                    break;
                                }
                                else if(amount<=10)
                                {
                                    mWeight = "KG";

                                }
                                else if(amount>10)
                                {
                                    mWeight = "Grams";
                                }
                            }

                        }
                        else if(amount<=10)
                        {
                            mWeight = "KG";

                        }
                        else if(amount>10)
                        {
                            mWeight = "Grams";
                        }
                    }
                    Element linePrice = el.getElementsByClass("linePrice").first();
                    String temp = linePrice.text();
                    mCurrency = "€";
                    mPrice = extractNumber(temp);
                    //Element linePriceAbbrv = el.getElementsByClass("linePriceAbbr").first();
                    mAmount.add(amount);
                    if(mWeight!=null)
                    {
                        mAmount.add(mWeight);
                    }
                    //mAmount.add(amounts);
                    TescoPrice price = new TescoPrice(name,mAmount,mCurrency, mPrice);
                    mTescoPricesReference.push().setValue(price);
                }
            }
        }

        public String extractNumber(final String str) {

            if (str == null || str.isEmpty()) return "";

            StringBuilder sb = new StringBuilder();
            char lastChar = ' ';
            for (char c : str.toCharArray()) {
                if (Character.isDigit(c) || c == '.' || c == '/' || c == '-') {
                    sb.append(c);
                    lastChar = c;
                } else if (c == '½' && Character.isDigit(lastChar)) {
                    sb.append(" ");
                    sb.append("1");
                    sb.append("/");
                    sb.append("2");
                } else if (c == '½') {
                    sb.append("1");
                    sb.append("/");
                    sb.append("2");
                } else if (c == '¼' && Character.isDigit(lastChar)) {
                    sb.append(" ");
                    sb.append("1");
                    sb.append("/");
                    sb.append("4");
                } else if (c == '¼') {
                    sb.append("1");
                    sb.append("/");
                    sb.append("4");
                } else if (c == '⅓' && Character.isDigit(lastChar)) {
                    sb.append(" ");
                    sb.append("1");
                    sb.append("/");
                    sb.append("3");
                } else if (c == '⅓') {
                    sb.append("1");
                    sb.append("/");
                    sb.append("3");
                } else if (c == '¾' && Character.isDigit(lastChar)) {
                    sb.append(" ");
                    sb.append("3");
                    sb.append("/");
                    sb.append("4");
                } else if (c == '¾') {
                    sb.append("3");
                    sb.append("/");
                    sb.append("4");
                }
            }

            return sb.toString();
        }

        public String extractServing(final String str) {

            if (str == null || str.isEmpty()) return "";

            StringBuilder sb = new StringBuilder();
            for (char c : str.toCharArray()) {
                if (Character.isDigit(c) == false && c != ' ') {
                    sb.append(c);

                }
            }

            return sb.toString();
        }
    }

}