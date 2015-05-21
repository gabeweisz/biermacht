package com.biermacht.brews.recipe;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.biermacht.brews.ingredient.Ingredient;
import com.biermacht.brews.utils.Units;

import java.util.ArrayList;
import java.util.HashMap;

public class Instruction implements Parcelable {

  private String instructionText;
  private String instructionType;
  private int order;
  private double duration;
  private String durationUnits;
  private double nextDuration;        // Time the next instruction starts
  private boolean lastInType;       // Last of this type
  private HashMap<String, Integer> typeToOrder;
  private ArrayList<Ingredient> relevantIngredients;
  private MashStep mashStep;     // Used for mash step instructions ONLY
  private Recipe r;         // Recipe to get important values for this instruction.

  public static String TYPE_STEEP = "Steep";
  public static String TYPE_BOIL = "Boil";
  public static String TYPE_PRIMARY = "1st";
  public static String TYPE_SECONDARY = "2nd";
  public static String TYPE_TERTIARY = "3rd";
  public static String TYPE_DRY_HOP = "Hop";
  public static String TYPE_MASH = "Mash";
  public static String TYPE_SPARGE = "Sparge";
  public static String TYPE_RAMP = "Ramp";
  public static String TYPE_YEAST = "Yeast";
  public static String TYPE_COOL = "Cool";
  public static String TYPE_BOTTLING = "Bottle";
  public static String TYPE_CALENDAR = "Calendar";
  public static String TYPE_OTHER = "";

  public Instruction(Recipe r) {
    this.r = r;
    this.setInstructionText("Blank Instruction");
    this.duration = 0;
    this.durationUnits = Units.MINUTES;
    this.order = - 1;
    this.instructionType = TYPE_OTHER;
    this.relevantIngredients = new ArrayList<Ingredient>();
    this.setLastInType(false);
    this.setNextDuration(0);
    this.mashStep = new MashStep(r);

    typeToOrder = new HashMap<String, Integer>();
    this.configureHashMap();
  }

  public Instruction(Parcel p) {
    // Initialize lists and maps
    this.typeToOrder = new HashMap<String, Integer>();
    this.relevantIngredients = new ArrayList<Ingredient>();

    instructionText = p.readString();
    instructionType = p.readString();
    order = p.readInt();
    duration = p.readDouble();
    durationUnits = p.readString();
    nextDuration = p.readDouble();
    lastInType = p.readInt() > 0;
    p.readTypedList(relevantIngredients, Ingredient.CREATOR);
    mashStep = p.readParcelable(MashStep.class.getClassLoader());
    r = p.readParcelable(Recipe.class.getClassLoader());

    this.configureHashMap();
  }

  @Override
  public void writeToParcel(Parcel p, int flags) {
    p.writeString(instructionText);
    p.writeString(instructionType);
    p.writeInt(order);
    p.writeDouble(duration);
    p.writeString(durationUnits);
    p.writeDouble(nextDuration);
    p.writeInt(lastInType ? 1 : 0);
    p.writeTypedList(relevantIngredients);
    p.writeParcelable(mashStep, flags);
    p.writeParcelable(r, flags);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public static final Parcelable.Creator<Instruction> CREATOR =
          new Parcelable.Creator<Instruction>() {
            @Override
            public Instruction createFromParcel(Parcel p) {
              return new Instruction(p);
            }

            @Override
            public Instruction[] newArray(int size) {
              return new Instruction[]{};
            }
          };

  public String getInstructionText() {
    return instructionText;
  }

  public void addToText(String s) {
    instructionText += s;
  }

  public void setMashStep(MashStep s) {
    this.mashStep = s;
  }

  public MashStep getMashStep() {
    return this.mashStep;
  }

  public String getBrewTimerTitle() {
    if (instructionType.equals(Instruction.TYPE_MASH)) {
      return mashStep.getName();
    }

    return this.instructionType;
  }

  public String getBrewTimerText() {
    String s = "";
    if (this.instructionType.equals(Instruction.TYPE_MASH)) {
      if (mashStep.getDisplayInfuseAmount() != 0) {
        s += "Add " + String.format("%2.2f", mashStep.getDisplayInfuseAmount()) + " " + Units.getVolumeUnits() + " of " +
                "" + String.format("%2.0f", mashStep.getDisplayInfuseTemp()) + Units.getTemperatureUnits() + "" +
                " water.\n\n";
      }

      if (mashStep.getDisplayDecoctAmount() != 0) {
        s += "Remove " + String.format("%2.0f", mashStep.getDisplayDecoctAmount()) + " " + Units.getVolumeUnits() +
                " of mash, and boil it.  Then, add it back to the mash.\n\n";
      }

      if (mashStep.getRampTime() != 0 && mashStep.getType().equals(MashStep.TEMPERATURE)) {
        s += "Adjust mash temperature to " + String.format("%2.0f", mashStep.getDisplayStepTemp()) + Units.getTemperatureUnits();
        s += " over " + mashStep.getRampTime() + " minutes.\n\n";
      }

      s += "Hold at " + String.format("%2.0f", mashStep.getDisplayStepTemp()) + Units.getTemperatureUnits();
      s += " for " + String.format("%2.0f", mashStep.getStepTime()) + " minutes.";
    }

    else if (this.instructionType.equals(Instruction.TYPE_STEEP)) {
      s += "Steep ingredients at " + r.getDisplaySteepTemp() + " " + Units.getTemperatureUnits();
    }

    else if (this.instructionType.equals(Instruction.TYPE_BOIL)) {
      s = "Add the ingredients shown below to the boil kettle.";
    }
    else if (this.instructionType.equals(Instruction.TYPE_PRIMARY)) {
    }
    else if (this.instructionType.equals(Instruction.TYPE_SECONDARY)) {
    }
    else if (this.instructionType.equals(Instruction.TYPE_DRY_HOP)) {
    }
    else if (this.instructionType.equals(Instruction.TYPE_RAMP)) {
    }
    else if (this.instructionType.equals(Instruction.TYPE_YEAST)) {
      s = "Add yeast, close your fermenter, and secure airlock. " +
              "Place your fermenter in a dark, temperature controlled " +
              "environment.";
    }
    else if (this.instructionType.equals(Instruction.TYPE_COOL)) {
      s = getInstructionText() + " as quickly as possible.  When cool, transfer wort into your " +
              "primary fermenter, and move to the next step.";
    }
    else if (this.instructionType.equals(Instruction.TYPE_BOTTLING)) {
    }
    else if (this.instructionType.equals(Instruction.TYPE_OTHER)) {
    }
    else if (this.instructionType.equals(Instruction.TYPE_SPARGE)) {
      s = getInstructionText() + " until you have " + String.format("%2.2f", r.getDisplayBoilSize()) +
              " " + Units.getVolumeUnits() + " of wort.  Then, add wort to the" +
              " boil kettle and bring to a steady boil.";
    }
    else if (this.instructionType.equals(Instruction.TYPE_CALENDAR)) {
      s = "Keep track of '" + r.getRecipeName() + "' in your calendar - click below!";
    }

    return s;
  }

  public int getOrder() {
    // If our order is over 100, mod it down so it fits within
    // the designated 100 orders per instruction type
    if (this.order >= 100 || this.order < 0) {
      this.order = this.order % 100;
    }

    try {
      return this.typeToOrder.get(this.getInstructionType()) + this.order;
    } catch (Exception e) {
      Log.d("recipe.Instruction", "Failed to get instruction order");
      return - 1;
    }
  }

  public void setOrder(int o) {
    this.order = o;
  }

  public void setNextDuration(double i) {
    this.nextDuration = i;
  }

  public double getNextDuration() {
    return this.nextDuration;
  }

  public void setLastInType(boolean b) {
    this.lastInType = b;
  }

  public boolean isLastInType() {
    return lastInType;
  }

  public void setRelevantIngredients(ArrayList<Ingredient> i) {
    this.relevantIngredients = i;
  }

  public ArrayList<Ingredient> getRelevantIngredients() {
    return this.relevantIngredients;
  }

  public void addRelevantIngredient(Ingredient i) {
    this.relevantIngredients.add(i);
  }

  @Override
  public String toString() {
    return this.getInstructionText();
  }

  public void setInstructionText(String instructionText) {
    this.instructionText = instructionText;
  }

  public void setInstructionTextFromIngredients() {
    String s = "";
    for (Ingredient i : this.getRelevantIngredients()) {
      if (s.isEmpty()) {
        s += i.getName();
      }
      else {
        s += "\n" + i.getName();
      }
    }
    this.instructionText = s;
  }

  public String getInstructionType() {
    return instructionType;
  }

  public void setInstructionType(String instructionType) {
    this.instructionType = instructionType;
  }

  public void setDuration(double d) {
    this.duration = d;
  }

  public boolean showInBrewTimer() {
    if (this.instructionType.equals(TYPE_BOIL)) {
      return true;
    }
    if (this.instructionType.equals(TYPE_STEEP)) {
      return true;
    }
    if (this.instructionType.equals(TYPE_MASH)) {
      return true;
    }
    if (this.instructionType.equals(TYPE_COOL)) {
      return true;
    }
    if (this.instructionType.equals(TYPE_YEAST)) {
      return true;
    }
    if (this.instructionType.equals(TYPE_CALENDAR)) {
      return true;
    }
    if (this.instructionType.equals(TYPE_SPARGE)) {
      return true;
    }

    return false;
  }

  public boolean showInInstructionList() {
    if (this.instructionType.equals(TYPE_CALENDAR)) {
      return false;
    }

    return true;
  }

  public boolean showTimer() {
    return duration > 0;
  }

  public double getDuration() {
    return duration;
  }

  public String getDurationUnits() {
    return durationUnits;
  }

  public double getTimeToNextStep() {
    return this.duration - this.nextDuration;
  }

  public void setDurationUnits(String d) {
    this.durationUnits = d;
  }

  private void configureHashMap() {
    int i = 0;
    this.typeToOrder.put(TYPE_OTHER, i);
    i += 100;
    this.typeToOrder.put(TYPE_STEEP, i);
    i += 100;
    this.typeToOrder.put(TYPE_MASH, i);
    i += 100;
    this.typeToOrder.put(TYPE_SPARGE, i);
    i += 100;
    this.typeToOrder.put(TYPE_BOIL, i);
    i += 100;
    this.typeToOrder.put(TYPE_COOL, i);
    i += 100;
    this.typeToOrder.put(TYPE_YEAST, i);
    i += 100;
    this.typeToOrder.put(TYPE_PRIMARY, i);
    i += 100;
    this.typeToOrder.put(TYPE_SECONDARY, i);
    i += 100;
    this.typeToOrder.put(TYPE_TERTIARY, i);
    i += 100;
    this.typeToOrder.put(TYPE_DRY_HOP, i);
    i += 100;
    this.typeToOrder.put(TYPE_BOTTLING, i);
    i += 100;
    this.typeToOrder.put(TYPE_CALENDAR, i);
    i += 100;
  }
}
