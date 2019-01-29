package ru.shtrm.serviceman.rfid;

/**
 * @author olejek
 * класс, описывающий структуру информации метки пользователя (бейджа)
 */
public class UserTagStructure {	
	protected String uuid;	// BDFF4E95-F0AB-4E07-9DFD-4CA314FFE05B16	
	protected String name;	// Логачев Дмитрий [20]

	public UserTagStructure() {
	}
	
	/**
	 * @return the user uuid
	 */
	public String get_user_uuid() {
		return uuid;
	}

	/**
	 * @param user_uuid the user_uuid to set
	 */
	public void set_user_uuid(String user_uuid) {
		this.uuid = user_uuid;
	}
	
	/**
	 * @return the user name
	 */
	public String get_name() {
		return name;
	}

	/**
	 * @param name name the user name to set
	 */
	public void set_name(String name) {
		this.name = name;
	}

}
