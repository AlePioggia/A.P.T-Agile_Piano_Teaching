package com.example.apt_agile_piano_teaching.logger;

import com.example.apt_agile_piano_teaching.models.UserLogTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
public final class CloudLogger {

    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore mDbReference = FirebaseFirestore.getInstance();

    public final void insertLog(Category category, Action action) {
        UserLogTemplate userLog = new UserLogTemplate(firebaseAuth.getCurrentUser().getEmail(),
                category.toString(), action.toString(), getGenericMessage(category, action), new Date());
        mDbReference.collection("logs")
                .document()
                .set(userLog);
    }

    public final void insertUserLog(String userMail, Action action) {
        UserLogTemplate userLog = new UserLogTemplate(userMail, Category.USER.toString(), action.toString(), getUserInsertMessage(userMail, action), new Date());
        mDbReference.collection("logs")
                .document()
                .set(userLog);
    }

    private final String getUserInsertMessage(String userMail, Action action) {
        return action.equals(Action.INSERT)
                ? "L'utente " + userMail + " si è registrato con successo!"
                : "L'utente " + userMail + " ha deciso di cancellare il proprio account";
    }

    private final String getGenericMessage(Category category, Action action) {
        return "L'utente " + firebaseAuth.getCurrentUser().getEmail() + " " + action.toString() + " un/una " + category.toString();
    }


}
