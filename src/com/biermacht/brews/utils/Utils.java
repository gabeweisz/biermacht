package com.biermacht.brews.utils;

import android.content.Context;

import com.biermacht.brews.database.DatabaseAPI;
import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.ingredient.Misc;
import com.biermacht.brews.recipe.Recipe;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
  /**
   * Determines if the given value is within the given range
   *
   * @param a
   * @param low
   * @param high
   * @return
   */
  public static boolean isWithinRange(double a, double low, double high) {
    if (a <= high && a >= low) {
      return true;
    }
    else {
      return false;
    }
  }

  public static int getHours(double time, String units) {
    int num_hours = 0;
    if (units.equals(Units.MINUTES)) {
      for (int i = 60; i <= time; i += 60) {
        num_hours++;
      }
    }
    if (units.equals(Units.HOURS)) {
      num_hours = (int) time;
    }
    return num_hours;
  }

  public static int getMinutes(double time, String units) {
    int num_minutes = 0;
    int num_hours = 0;

    if (units.equals(Units.HOURS)) {
      time = 60 * time;
      units = Units.MINUTES;
    }

    if (units.equals(Units.MINUTES)) {
      num_hours = getHours(time, units);
      num_minutes = (int) (time - (60 * num_hours));
    }

    return num_minutes;
  }

  public static String convertStreamToString(InputStream is) throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null) {
      sb.append(line).append("\n");
    }
    reader.close();
    return sb.toString();
  }

  /**
   * @return Beer XML standard version in use
   */
  public static int getXmlVersion() {
    return 1;
  }

  public static Recipe scaleRecipe(Context c, Recipe r, double newVolume) {
    double oldVolume = r.getDisplayBatchSize();
    double ratio = newVolume / oldVolume;

    r.setDisplayBatchSize(newVolume);
    r.setDisplayBoilSize(r.getDisplayBoilSize() * ratio);

    for (Ingredient i : r.getIngredientList()) {
      if (i instanceof Misc) {
        ((Misc) i).setDisplayAmount(i.getDisplayAmount() * ratio, i.getDisplayUnits());
      }
      else {
        i.setDisplayAmount(i.getDisplayAmount() * ratio);
      }
      new DatabaseAPI(c).updateIngredient(i, Constants.DATABASE_DEFAULT);
    }

    r.save(c);
    return r;
  }
}
