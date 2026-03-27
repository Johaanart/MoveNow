package com.example.movenow;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.navigation.NavArgs;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class StartFragmentArgs implements NavArgs {
  private final HashMap arguments = new HashMap();

  private StartFragmentArgs() {
  }

  @SuppressWarnings("unchecked")
  private StartFragmentArgs(HashMap argumentsMap) {
    this.arguments.putAll(argumentsMap);
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static StartFragmentArgs fromBundle(@NonNull Bundle bundle) {
    StartFragmentArgs __result = new StartFragmentArgs();
    bundle.setClassLoader(StartFragmentArgs.class.getClassLoader());
    if (bundle.containsKey("USER_HAS_EXERCISED")) {
      boolean USERHASEXERCISED;
      USERHASEXERCISED = bundle.getBoolean("USER_HAS_EXERCISED");
      __result.arguments.put("USER_HAS_EXERCISED", USERHASEXERCISED);
    } else {
      __result.arguments.put("USER_HAS_EXERCISED", false);
    }
    if (bundle.containsKey("USER_HAS_PAIN")) {
      boolean USERHASPAIN;
      USERHASPAIN = bundle.getBoolean("USER_HAS_PAIN");
      __result.arguments.put("USER_HAS_PAIN", USERHASPAIN);
    } else {
      __result.arguments.put("USER_HAS_PAIN", false);
    }
    if (bundle.containsKey("USER_INTENSITY")) {
      String USERINTENSITY;
      USERINTENSITY = bundle.getString("USER_INTENSITY");
      if (USERINTENSITY == null) {
        throw new IllegalArgumentException("Argument \"USER_INTENSITY\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("USER_INTENSITY", USERINTENSITY);
    } else {
      __result.arguments.put("USER_INTENSITY", "Suave");
    }
    if (bundle.containsKey("USER_GOAL")) {
      String USERGOAL;
      USERGOAL = bundle.getString("USER_GOAL");
      if (USERGOAL == null) {
        throw new IllegalArgumentException("Argument \"USER_GOAL\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("USER_GOAL", USERGOAL);
    } else {
      __result.arguments.put("USER_GOAL", "Fuerza");
    }
    return __result;
  }

  @NonNull
  @SuppressWarnings("unchecked")
  public static StartFragmentArgs fromSavedStateHandle(@NonNull SavedStateHandle savedStateHandle) {
    StartFragmentArgs __result = new StartFragmentArgs();
    if (savedStateHandle.contains("USER_HAS_EXERCISED")) {
      boolean USERHASEXERCISED;
      USERHASEXERCISED = savedStateHandle.get("USER_HAS_EXERCISED");
      __result.arguments.put("USER_HAS_EXERCISED", USERHASEXERCISED);
    } else {
      __result.arguments.put("USER_HAS_EXERCISED", false);
    }
    if (savedStateHandle.contains("USER_HAS_PAIN")) {
      boolean USERHASPAIN;
      USERHASPAIN = savedStateHandle.get("USER_HAS_PAIN");
      __result.arguments.put("USER_HAS_PAIN", USERHASPAIN);
    } else {
      __result.arguments.put("USER_HAS_PAIN", false);
    }
    if (savedStateHandle.contains("USER_INTENSITY")) {
      String USERINTENSITY;
      USERINTENSITY = savedStateHandle.get("USER_INTENSITY");
      if (USERINTENSITY == null) {
        throw new IllegalArgumentException("Argument \"USER_INTENSITY\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("USER_INTENSITY", USERINTENSITY);
    } else {
      __result.arguments.put("USER_INTENSITY", "Suave");
    }
    if (savedStateHandle.contains("USER_GOAL")) {
      String USERGOAL;
      USERGOAL = savedStateHandle.get("USER_GOAL");
      if (USERGOAL == null) {
        throw new IllegalArgumentException("Argument \"USER_GOAL\" is marked as non-null but was passed a null value.");
      }
      __result.arguments.put("USER_GOAL", USERGOAL);
    } else {
      __result.arguments.put("USER_GOAL", "Fuerza");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  public boolean getUSERHASEXERCISED() {
    return (boolean) arguments.get("USER_HAS_EXERCISED");
  }

  @SuppressWarnings("unchecked")
  public boolean getUSERHASPAIN() {
    return (boolean) arguments.get("USER_HAS_PAIN");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getUSERINTENSITY() {
    return (String) arguments.get("USER_INTENSITY");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public String getUSERGOAL() {
    return (String) arguments.get("USER_GOAL");
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public Bundle toBundle() {
    Bundle __result = new Bundle();
    if (arguments.containsKey("USER_HAS_EXERCISED")) {
      boolean USERHASEXERCISED = (boolean) arguments.get("USER_HAS_EXERCISED");
      __result.putBoolean("USER_HAS_EXERCISED", USERHASEXERCISED);
    } else {
      __result.putBoolean("USER_HAS_EXERCISED", false);
    }
    if (arguments.containsKey("USER_HAS_PAIN")) {
      boolean USERHASPAIN = (boolean) arguments.get("USER_HAS_PAIN");
      __result.putBoolean("USER_HAS_PAIN", USERHASPAIN);
    } else {
      __result.putBoolean("USER_HAS_PAIN", false);
    }
    if (arguments.containsKey("USER_INTENSITY")) {
      String USERINTENSITY = (String) arguments.get("USER_INTENSITY");
      __result.putString("USER_INTENSITY", USERINTENSITY);
    } else {
      __result.putString("USER_INTENSITY", "Suave");
    }
    if (arguments.containsKey("USER_GOAL")) {
      String USERGOAL = (String) arguments.get("USER_GOAL");
      __result.putString("USER_GOAL", USERGOAL);
    } else {
      __result.putString("USER_GOAL", "Fuerza");
    }
    return __result;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  public SavedStateHandle toSavedStateHandle() {
    SavedStateHandle __result = new SavedStateHandle();
    if (arguments.containsKey("USER_HAS_EXERCISED")) {
      boolean USERHASEXERCISED = (boolean) arguments.get("USER_HAS_EXERCISED");
      __result.set("USER_HAS_EXERCISED", USERHASEXERCISED);
    } else {
      __result.set("USER_HAS_EXERCISED", false);
    }
    if (arguments.containsKey("USER_HAS_PAIN")) {
      boolean USERHASPAIN = (boolean) arguments.get("USER_HAS_PAIN");
      __result.set("USER_HAS_PAIN", USERHASPAIN);
    } else {
      __result.set("USER_HAS_PAIN", false);
    }
    if (arguments.containsKey("USER_INTENSITY")) {
      String USERINTENSITY = (String) arguments.get("USER_INTENSITY");
      __result.set("USER_INTENSITY", USERINTENSITY);
    } else {
      __result.set("USER_INTENSITY", "Suave");
    }
    if (arguments.containsKey("USER_GOAL")) {
      String USERGOAL = (String) arguments.get("USER_GOAL");
      __result.set("USER_GOAL", USERGOAL);
    } else {
      __result.set("USER_GOAL", "Fuerza");
    }
    return __result;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
        return true;
    }
    if (object == null || getClass() != object.getClass()) {
        return false;
    }
    StartFragmentArgs that = (StartFragmentArgs) object;
    if (arguments.containsKey("USER_HAS_EXERCISED") != that.arguments.containsKey("USER_HAS_EXERCISED")) {
      return false;
    }
    if (getUSERHASEXERCISED() != that.getUSERHASEXERCISED()) {
      return false;
    }
    if (arguments.containsKey("USER_HAS_PAIN") != that.arguments.containsKey("USER_HAS_PAIN")) {
      return false;
    }
    if (getUSERHASPAIN() != that.getUSERHASPAIN()) {
      return false;
    }
    if (arguments.containsKey("USER_INTENSITY") != that.arguments.containsKey("USER_INTENSITY")) {
      return false;
    }
    if (getUSERINTENSITY() != null ? !getUSERINTENSITY().equals(that.getUSERINTENSITY()) : that.getUSERINTENSITY() != null) {
      return false;
    }
    if (arguments.containsKey("USER_GOAL") != that.arguments.containsKey("USER_GOAL")) {
      return false;
    }
    if (getUSERGOAL() != null ? !getUSERGOAL().equals(that.getUSERGOAL()) : that.getUSERGOAL() != null) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = 31 * result + (getUSERHASEXERCISED() ? 1 : 0);
    result = 31 * result + (getUSERHASPAIN() ? 1 : 0);
    result = 31 * result + (getUSERINTENSITY() != null ? getUSERINTENSITY().hashCode() : 0);
    result = 31 * result + (getUSERGOAL() != null ? getUSERGOAL().hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "StartFragmentArgs{"
        + "USERHASEXERCISED=" + getUSERHASEXERCISED()
        + ", USERHASPAIN=" + getUSERHASPAIN()
        + ", USERINTENSITY=" + getUSERINTENSITY()
        + ", USERGOAL=" + getUSERGOAL()
        + "}";
  }

  public static final class Builder {
    private final HashMap arguments = new HashMap();

    @SuppressWarnings("unchecked")
    public Builder(@NonNull StartFragmentArgs original) {
      this.arguments.putAll(original.arguments);
    }

    public Builder() {
    }

    @NonNull
    public StartFragmentArgs build() {
      StartFragmentArgs result = new StartFragmentArgs(arguments);
      return result;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setUSERHASEXERCISED(boolean USERHASEXERCISED) {
      this.arguments.put("USER_HAS_EXERCISED", USERHASEXERCISED);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setUSERHASPAIN(boolean USERHASPAIN) {
      this.arguments.put("USER_HAS_PAIN", USERHASPAIN);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setUSERINTENSITY(@NonNull String USERINTENSITY) {
      if (USERINTENSITY == null) {
        throw new IllegalArgumentException("Argument \"USER_INTENSITY\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("USER_INTENSITY", USERINTENSITY);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public Builder setUSERGOAL(@NonNull String USERGOAL) {
      if (USERGOAL == null) {
        throw new IllegalArgumentException("Argument \"USER_GOAL\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("USER_GOAL", USERGOAL);
      return this;
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public boolean getUSERHASEXERCISED() {
      return (boolean) arguments.get("USER_HAS_EXERCISED");
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    public boolean getUSERHASPAIN() {
      return (boolean) arguments.get("USER_HAS_PAIN");
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getUSERINTENSITY() {
      return (String) arguments.get("USER_INTENSITY");
    }

    @SuppressWarnings({"unchecked","GetterOnBuilder"})
    @NonNull
    public String getUSERGOAL() {
      return (String) arguments.get("USER_GOAL");
    }
  }
}
