package gestions.ferme.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextFieldAPIValidator {
    
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    
    /**
     * Vérifie si un champ de texte contient des gros mots
     * @param textField Le champ de texte à vérifier
     * @param callback Callback appelé avec le résultat de la validation
     */
    public static void validateNoProfanity(TextInputControl textField, ValidationCallback callback) {
        String text = textField.getText();
        
        if (text == null || text.trim().isEmpty()) {
            callback.onResult(true);
            return;
        }
        
        System.out.println("Validation du texte: " + text); // Log pour débogage
        
        // Afficher un indicateur de chargement
        textField.setStyle("-fx-border-color: orange; -fx-border-width: 1px;");
        
        // Exécuter la vérification dans un thread séparé pour ne pas bloquer l'UI
        executor.submit(() -> {
            boolean containsProfanity = ProfanityAPI.containsProfanity(text);
            System.out.println("Résultat de la validation: " + (containsProfanity ? "Contient des gros mots" : "Pas de gros mots")); // Log pour débogage
            
            // Revenir au thread UI pour mettre à jour l'interface
            Platform.runLater(() -> {
                if (containsProfanity) {
                    showProfanityAlert();
                    textField.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
                    callback.onResult(false);
                } else {
                    textField.setStyle("");
                    callback.onResult(true);
                }
            });
        });
    }
    
    /**
     * Affiche une alerte pour informer l'utilisateur qu'un gros mot a été détecté
     */
    private static void showProfanityAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Langage inapproprié");
        alert.setHeaderText("Contenu inapproprié détecté");
        alert.setContentText("Veuillez éviter d'utiliser un langage inapproprié dans les champs de texte.");
        alert.showAndWait();
    }
    
    /**
     * Ajoute un écouteur à un champ de texte pour valider lors de la perte de focus
     * @param textField Le champ de texte à surveiller
     */
    public static void addProfanityListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            // Valider lorsque le champ perd le focus
            if (!newValue && !textField.getText().isEmpty()) {
                System.out.println("Perte de focus sur le champ: " + textField.getId()); // Log pour débogage
                validateNoProfanity(textField, result -> {
                    // Optionnel: effacer le texte si des gros mots sont détectés
                    if (!result) {
                        textField.clear();
                    }
                });
            }
        });
    }
    
    /**
     * Interface pour le callback de validation
     */
    public interface ValidationCallback {
        void onResult(boolean isValid);
    }
}