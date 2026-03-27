package com.example.movenow;

import android.os.Bundle;
import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;
import androidx.navigation.ActionOnlyNavDirections;
import androidx.navigation.NavDirections;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;

public class HomeFragmentDirections {
  private HomeFragmentDirections() {
  }

  @CheckResult
  @NonNull
  public static NavDirections actionHomeFragmentToQuestionnaireFragment() {
    return new ActionOnlyNavDirections(R.id.action_homeFragment_to_questionnaireFragment);
  }

  @CheckResult
  @NonNull
  public static ActionHomeFragmentToStartFragment actionHomeFragmentToStartFragment() {
    return new ActionHomeFragmentToStartFragment();
  }

  public static class ActionHomeFragmentToStartFragment implements NavDirections {
    private final HashMap arguments = new HashMap();

    private ActionHomeFragmentToStartFragment() {
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionHomeFragmentToStartFragment setUSERHASEXERCISED(boolean USERHASEXERCISED) {
      this.arguments.put("USER_HAS_EXERCISED", USERHASEXERCISED);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionHomeFragmentToStartFragment setUSERHASPAIN(boolean USERHASPAIN) {
      this.arguments.put("USER_HAS_PAIN", USERHASPAIN);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionHomeFragmentToStartFragment setUSERINTENSITY(@NonNull String USERINTENSITY) {
      if (USERINTENSITY == null) {
        throw new IllegalArgumentException("Argument \"USER_INTENSITY\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("USER_INTENSITY", USERINTENSITY);
      return this;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public ActionHomeFragmentToStartFragment setUSERGOAL(@NonNull String USERGOAL) {
      if (USERGOAL == null) {
        throw new IllegalArgumentException("Argument \"USER_GOAL\" is marked as non-null but was passed a null value.");
      }
      this.arguments.put("USER_GOAL", USERGOAL);
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    @NonNull
    public Bundle getArguments() {
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

    @Override
    public int getActionId() {
      return R.id.action_homeFragment_to_startFragment;
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

    @Override
    public boolean equals(Object object) {
      if (this == object) {
          return true;
      }
      if (object == null || getClass() != object.getClass()) {
          return false;
      }
      ActionHomeFragmentToStartFragment that = (ActionHomeFragmentToStartFragment) object;
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
      if (getActionId() != that.getActionId()) {
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
      result = 31 * result + getActionId();
      return result;
    }

    @Override
    public String toString() {
      return "ActionHomeFragmentToStartFragment(actionId=" + getActionId() + "){"
          + "USERHASEXERCISED=" + getUSERHASEXERCISED()
          + ", USERHASPAIN=" + getUSERHASPAIN()
          + ", USERINTENSITY=" + getUSERINTENSITY()
          + ", USERGOAL=" + getUSERGOAL()
          + "}";
    }
  }
}
