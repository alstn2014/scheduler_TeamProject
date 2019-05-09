package scheduler.member.manager;

import java.util.regex.Pattern;

public class MemberDataCheck {

	public static boolean checkPhone(String phoneNum) {

		boolean check1 = phoneNum.length() >= 5;
		boolean check2 = Pattern.matches("^[0-9]{2,4}-[0-9]{2,4}-[0-9]{2,4}$", phoneNum);
		boolean check3 = Pattern.matches("^[0-9]{2,4}[0-9]{2,4}[0-9]{2,4}$", phoneNum);
		return check1 && (check2 || check3);
	}

	public static boolean checkEmail(String email) {

		boolean check1 = email.length() > 3;
		boolean check2 = Pattern.matches(
				"^([\\w-]+(?:\\.[\\w-]+)*)@((?:[\\w-]+\\.)*\\w[\\w-]{0,66})\\.([a-z]{2,6}(?:\\.[a-z]{2})?)$", email);
		return check1 && check2;
	}

}
