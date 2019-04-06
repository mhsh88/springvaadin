/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ir.sharifi.spring.i18n;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.vaadin.flow.i18n.I18NProvider;

/**
 * Simple implementation of {@link I18NProvider}.
 */
@Component
public class SimpleI18NProvider implements I18NProvider {
    @Autowired
    MessageSource messageSource;

    @Override
    public List<Locale> getProvidedLocales() {
        return Collections.unmodifiableList(
                Arrays.asList(Locale.ENGLISH, Locale.CHINESE,new Locale("fa","IR")));
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        return messageSource.getMessage(key, null, locale);
//        if (Locale.ENGLISH.equals(locale)) {
//            if (key.equals("root.navigate_to_component")) {
//                return "Navigate to another component";
//            }
//        } else if (Locale.CHINESE.equals(locale)) {
//            if (key.equals("root.navigate_to_component")) {
//                return "前往另一视图";
//            }
//        }
//        return key;
//    }
    }


}
