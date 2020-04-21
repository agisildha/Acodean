package id.putraprima.retrofit.api.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Error {

    @SerializedName("email")
    @Expose
    private List<String> email = null;

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    @SerializedName("password")
    @Expose
    private List<String> password = null;

    public List<String> getPassword() {
        return password;
    }

    public void setPassword(List<String> password){
        this.password = password;
    }

    @SerializedName("name")
    @Expose
    private List<String> nama = null;

    public List<String> getName() {
        return nama;
    }

    public void setName(List<String> nama) {
        this.nama = nama;
    }
}