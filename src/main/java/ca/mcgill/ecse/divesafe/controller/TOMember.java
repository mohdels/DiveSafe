/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language!*/

package ca.mcgill.ecse.divesafe.controller;

/**
 * @author Mohammed Elsayed
 */
// line 23 "../../../../../DiveSafeTransferObjects.ump"
public class TOMember
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //TOMember Attributes
  private String email;
  private String name;
  private String emergancyContact;
  private String password;
  private int days;
  private boolean needGuide;
  private boolean needHotel;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public TOMember(String aEmail, String aName, String aEmergancyContact, String aPassword, int aDays, boolean aNeedGuide, boolean aNeedHotel)
  {
    email = aEmail;
    name = aName;
    emergancyContact = aEmergancyContact;
    password = aPassword;
    days = aDays;
    needGuide = aNeedGuide;
    needHotel = aNeedHotel;
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setEmail(String aEmail)
  {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName)
  {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setEmergancyContact(String aEmergancyContact)
  {
    boolean wasSet = false;
    emergancyContact = aEmergancyContact;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword)
  {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public boolean setDays(int aDays)
  {
    boolean wasSet = false;
    days = aDays;
    wasSet = true;
    return wasSet;
  }

  public boolean setNeedGuide(boolean aNeedGuide)
  {
    boolean wasSet = false;
    needGuide = aNeedGuide;
    wasSet = true;
    return wasSet;
  }

  public boolean setNeedHotel(boolean aNeedHotel)
  {
    boolean wasSet = false;
    needHotel = aNeedHotel;
    wasSet = true;
    return wasSet;
  }

  public String getEmail()
  {
    return email;
  }

  public String getName()
  {
    return name;
  }

  public String getEmergancyContact()
  {
    return emergancyContact;
  }

  public String getPassword()
  {
    return password;
  }

  public int getDays()
  {
    return days;
  }

  public boolean getNeedGuide()
  {
    return needGuide;
  }

  public boolean getNeedHotel()
  {
    return needHotel;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isNeedGuide()
  {
    return needGuide;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isNeedHotel()
  {
    return needHotel;
  }

  public void delete()
  {}


  public String toString()
  {
    return super.toString() + "["+
            "email" + ":" + getEmail()+ "," +
            "name" + ":" + getName()+ "," +
            "emergancyContact" + ":" + getEmergancyContact()+ "," +
            "password" + ":" + getPassword()+ "," +
            "days" + ":" + getDays()+ "," +
            "needGuide" + ":" + getNeedGuide()+ "," +
            "needHotel" + ":" + getNeedHotel()+ "]";
  }
}