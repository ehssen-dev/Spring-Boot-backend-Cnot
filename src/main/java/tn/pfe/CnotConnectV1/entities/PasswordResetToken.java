package tn.pfe.CnotConnectV1.entities;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long token_id;
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 10;

    @OneToOne
    @JoinColumn(name = "athlete_id")
    private Athlete athlete;
    @OneToOne
    @JoinColumn(name = "delegation_id")
    private Delegation delegation;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    
    public PasswordResetToken(String token, Athlete athlete,Delegation delegation,
    		User user ) {
        super();
        this.token = token;
        this.athlete = athlete;
        this.delegation = delegation;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public PasswordResetToken(String token, Athlete athlete) {
        super();
        this.token = token;
        this.athlete = athlete;
        this.expirationTime = this.getTokenExpirationTime();
    }
    public PasswordResetToken(String token, Delegation delegation) {
        super();
        this.token = token;
        this.delegation = delegation;
        this.expirationTime = this.getTokenExpirationTime();
    }
    public PasswordResetToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }
   
    public PasswordResetToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }


}
