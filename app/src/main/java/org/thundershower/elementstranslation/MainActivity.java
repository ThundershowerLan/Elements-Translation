package org.thundershower.elementstranslation;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;

import android.text.Editable;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.thundershower.elementstranslation.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final var binding = ActivityMainBinding.inflate(getLayoutInflater());
        final var toElement = new HashMap<String, String>();
        final var toEnglish = new HashMap<String, String>();

        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        for (String item: getResources().getStringArray(R.array.elements)) {
            final var element = String.valueOf(item.charAt(0));
            final var english = item.substring(1);

            toElement.put(english, element);
            toEnglish.put(element, english);
        }

        binding.toElements.setOnClickListener(_ -> {
            final var input = Optional.ofNullable(binding.inputEdit.getText()).map(Editable::toString).orElse("").split("");
            final var result = new StringBuilder();

            for (int index = 0; index < input.length; index++) {
                if (input.length > index+1) {
                    final var optional = Optional.ofNullable(toElement.get(input[index].toUpperCase() + input[index+1]));
                    if (optional.isPresent()) {
                        result.append(optional.get());
                        index++;
                        continue;
                    }
                }

                result.append(toElement.getOrDefault(input[index].toUpperCase(), input[index]));
            }

            binding.resultEdit.setText(result);
        });

        binding.toEnglish.setOnClickListener(_ -> {
            final var input = Optional.ofNullable(binding.inputEdit.getText()).map(Editable::toString).orElse("").split("");
            final var result = new StringBuilder();

            for (String word : input)
                result.append(toEnglish.getOrDefault(word, word));

            binding.resultEdit.setText(result.toString().toLowerCase());
        });

        binding.copy.setOnClickListener(_ -> ((ClipboardManager) getSystemService(CLIPBOARD_SERVICE)).
                setPrimaryClip(ClipData.newPlainText("From Elements Translation",
                Optional.ofNullable(binding.resultEdit.getText())
                        .map(Editable::toString)
                        .orElse(""))));

        binding.clear.setOnClickListener(_ -> {
            binding.inputEdit.setText("");
            binding.resultEdit.setText("");
        });
    }
}