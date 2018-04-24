/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm;

/**
 *
 * @author Ivan
 */
public class JAPPAddress {

   // public int addressId = -1;
    public String nameFirst = "";
    public String nameLast = "";
    public String email = "New Address";

    public JAPPAddress() {
    }

    public JAPPAddress(String nameFirst, String nameLast, String email) {
        this.nameFirst = nameFirst;
        this.nameLast = nameLast;
        this.email = email;
    }


    public String toString() {

        if (this.nameFirst.isEmpty() && this.nameLast.isEmpty()) {
            return this.email;
        }

        return this.nameFirst + " " + nameLast + " <" + email + ">";

    }
}
