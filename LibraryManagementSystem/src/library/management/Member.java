package library.management;
public class Member {
    private String memberId;
    private String name;
    private String mail;
    private String phone;

    public Member(String memberId, String name, String mail, String phone) {
        this.memberId = memberId;
        this.name = name;
        this.mail = mail;
        this.phone = phone;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }
}
