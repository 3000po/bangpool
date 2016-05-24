package kr.popcorn.sharoom.helper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

/**
 * Created by user on 16. 2. 22.
 */

//TODO 전화번호 검사를 해줘야함
public class Helper_checker {
    public static final int MIN = 5;
    public static final int MAX = 20;
    public static final int NAMEMIN = 2;
    public static final int NAMEMAX = 20;

    public static boolean isEmail(String email) {
        if (email == null) return false;
        boolean b = Pattern.matches("[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+", email.trim());
        return b;
    }

    public static boolean validName(String name) {
        if (name == null) return false;
        if (name.length() < NAMEMIN || name.length() > NAMEMAX) return false;
        return true;
    }

    public static boolean validId(String id) {
        //TODO 아이디 중복 검사
        if (id == null) return false;
        if (id.length() < MIN || id.length() > MAX) return false;
        return true;
    }

    public static boolean onlyNumberId(String id) {
        if (Pattern.matches("^[0-9]+$", id)) return false;
        return true;
    }


    public static boolean validPassword(String pw) {
        if (pw == null) return false;
        if (pw.length() < MIN) return false;
        return true;
    }

    public static boolean validId_context(Context context, String id) {
        if (!validId(id)) {
            Toast.makeText(context, "아이디는 5글자이상 20글자 이하여야합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!onlyNumberId(id)) {
            Toast.makeText(context, "아이디는 숫자로만 이루어지면 안됩니다. ", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    public static boolean id_check_ok(Context context, boolean id_check) {
        if (!id_check) {
            Toast.makeText(context, "중복된 아이디 입니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean id_error_check(String id) {
        if (id.contains("'")) return false;
        if (id.contains("\"")) return false;
        if (id == null) return false;
        return true;
    }

    public static boolean password_error_check(String pw) {
        if (pw.contains("'")) return false;
        if (pw.contains("\"")) return false;
        return true;
    }

    public static boolean email_error_check(String email) {
        if (email.contains("'")) return false;
        if (email.contains("\"")) return false;
        return true;
    }

    public static boolean name_error_check(String name) {
        if (name.contains("'")) return false;
        if (name.contains("\"")) return false;
        return true;
    }

    public static boolean null_error_check(String email, String name, String id, String pw) {
        if (id == "") return false;
        if (email == "") return false;
        if (pw == "") return false;
        if (name == "") return false;
        return true;
    }

    public static boolean phone_number_check(String pn) {
        if (!Pattern.matches("^[0-9]*$", pn)) return false;
        if (!(10 <= pn.length() && pn.length() <= 11)) return false;
        return true;
    }


    public static boolean validJoin(Context context, String email, String name, String id, String pw, String pn) {
        if (!validId(id)) {
            Toast.makeText(context, "아이디는 5글자이상 20글자 이하여야합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!onlyNumberId(id)) {
            Toast.makeText(context, "아이디는 숫자로만 이루어지면 안됩니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validPassword(pw)) {
            Toast.makeText(context, "비밀번호는 5글자 이상이어야합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!validName(name)) {
            Toast.makeText(context, "이름은 2글자이상 20글자 이하여야합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!isEmail(email)) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!id_error_check(id)) {
            Toast.makeText(context, " 아이디에 ', \" 형식이 있으면 안됩니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!password_error_check(pw)) {
            Toast.makeText(context, " 비밀번호에 ', \" 형식이 있으면 안됩니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!email_error_check(email)) {
            Toast.makeText(context, " 이메일에 ', \" 형식이 있으면 안됩니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!name_error_check(name)) {
            Toast.makeText(context, " 이름에 ', \" 형식이 있으면 안됩니다. ", Toast.LENGTH_LONG).show();
            return false;
        }

        if (!null_error_check(email, name, id, pw)) {
            Toast.makeText(context, " 빈칸이 있으면 안됩니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!phone_number_check(pn)) {
            Toast.makeText(context, " 휴대폰 형식에 맞게 입력해주세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean validRegister(Context context, String title, String address, String price, String fac, String roomInfo) {

        //제목
        if (title.length() < 1) {
            Toast.makeText(context, " 제목이 비어있습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (title.length() > 50) {
            Toast.makeText(context, " 제목이 50자 넘으면 안 됩니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (title.contains("'") || title.contains("\"")) {
            Toast.makeText(context, " 제목에 [ ', \" ] 가 없어야 합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }

        //주소
        if (address.length() < 1) {
            Toast.makeText(context, " 주소가 비어있습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (address.length() > 50) {
            Toast.makeText(context, " 주소가 50자 넘으면 안 됩니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (address.contains("'") || address.contains("\"")) {
            Toast.makeText(context, " 주소에 [ ', \" ] 가 없어야 합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }

        //가격
        if (price.length() < 1) {
            Toast.makeText(context, " 가격이 비어있습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (price.length() > 20) {
            Toast.makeText(context, " 가격이 10,000,000,000,000,000,000 넘으면 안 됩니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (!Pattern.matches("^[0-9]*$", price)) {
            Toast.makeText(context, " 가격은 숫자만 입력해야 합니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (price.contains("'") || price.contains("\"")) {
            Toast.makeText(context, " 가격에 [ ', \" ] 가 없어야 합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }

        //편의시설
        if (fac.length() < 1) {
            Toast.makeText(context, " 편의시설이 비어있습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (fac.length() > 50) {
            Toast.makeText(context, " 편의시설이 50자 넘으면 안 됩니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (fac.contains("'") || fac.contains("\"")) {
            Toast.makeText(context, " 편의시설에 [ ', \" ] 가 없어야 합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }

        //방소개
        if (roomInfo.length() < 1) {
            Toast.makeText(context, " 방 소개가 비어있습니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (roomInfo.length() > 110) {
            Toast.makeText(context, " 방 소개가 110자 넘으면 안 됩니다.", Toast.LENGTH_LONG).show();
            return false;
        }
        if (roomInfo.contains("'") || roomInfo.contains("\"")) {
            Toast.makeText(context, " 방 소개에 [ ', \" ] 가 없어야 합니다. ", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}