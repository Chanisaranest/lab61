package com.example.lab61.view;

import com.example.lab61.pojo.Wizard;
import com.example.lab61.pojo.Wizards;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;


@Route(value = "/mainPage.it")
public class MainWizardView extends VerticalLayout {

    private Notification noti;
    private RadioButtonGroup<String> gender;
    private ComboBox position, school, house;
    private TextField fullName,dollars;
    private Button create,update,del,left,right;
    private Wizards wizards;
    private int index = 0;
    public MainWizardView(){
        wizards = new Wizards();
        gender = new RadioButtonGroup<>();
        gender.setLabel("Gender:");
        gender.setItems("Male" , "Female");

        position = new ComboBox<>();
        position.setPlaceholder("Position");
        position.setItems("Student", "Teacher");
        school = new ComboBox<>();
        school.setPlaceholder("School");
        school.setItems("Hogwarts","Beauxbatons","Durmstrang");
        house = new ComboBox<>();
        house.setPlaceholder("House");
        house.setItems("Gryffindor", "Ravenchaw", "Hufflepuff","Slyther");

        fullName = new TextField();
        fullName.setPlaceholder("Fullname");
        dollars = new TextField("Dollars");
        dollars.setPrefixComponent(new Span("$"));

        left = new Button("<<");
        create = new Button("Create");
        update = new Button("Update");
        del = new Button("Delete");
        right = new Button(">>");


        HorizontalLayout h1 = new HorizontalLayout();
        h1.add(left,create,update,del,right);
        add(fullName,gender,position,dollars,school,house,h1);
        this.fetchData();

        create.addClickListener(event -> {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("sex",  gender.getValue().equals("Male") ? "m" : "f");
            formData.add("name", fullName.getValue());
            formData.add("school", school.getValue().toString());
            formData.add("house", house.getValue().toString());
            formData.add("money", dollars.getValue());
            formData.add("position", position.getValue().toString());

            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            Notification noti = Notification.show(out);
            this.fetchData();
            this.onTimeData();
        });

        update.addClickListener(event -> {
            String sex = gender.getValue().equals("Male") ? "m" : "f";
            String name = fullName.getValue();
            String sc = school.getValue().toString();
            String ho = house.getValue().toString();
            int money = Integer.parseInt(dollars.getValue());
            String posi = position.getValue().toString();
            Wizard update = new Wizard(wizards.getModel().get(index).get_id(), sex, name, sc, ho, money, posi);

            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(update), Wizard.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            Notification noti = Notification.show(out);
            this.fetchData();
            this.onTimeData();
        });

        del.addClickListener(event -> {
            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .body(Mono.just(wizards.getModel().get(index)), Wizard.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            Notification noti = Notification.show(out);
            this.index = this.index != 0 ? this.index-1 : this.index+1;
            this.fetchData();
            this.onTimeData();
        });


        left.addClickListener(event -> {
            if (index == 0){
                index = 0;
            }
            else{
                index = index - 1;
            }
            this.onTimeData();
        });

        right.addClickListener(event -> {
            if (index == wizards.getModel().size()-1){
                index = wizards.getModel().size()-1;
            }
            else{
                index = index + 1;
            }
            this.onTimeData();
        });
    }
    private void fetchData(){
        ArrayList<Wizard> allWizards = WebClient.create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Wizard>>() {})
                .block();
        wizards.setModel(allWizards);
    }

    private void onTimeData(){
        if (wizards.getModel().size() != 0){
            this.fullName.setValue(wizards.getModel().get(index).getName());
            this.gender.setValue(wizards.getModel().get(index).getSex().equals("m") ? "Male" : "Female");
            this.position.setValue(wizards.getModel().get(index).getPosition().equals("teacher") ? "Teacher" : "Student");
            this.dollars.setValue(String.valueOf(wizards.getModel().get(index).getMoney()));
            this.school.setValue(wizards.getModel().get(index).getSchool());
            this.house.setValue(wizards.getModel().get(index).getHouse());
        }
        else{
            this.fullName.setValue("");
            this.gender.setValue("");
            this.position.setValue("");
            this.dollars.setValue("");
            this.school.setValue("");
            this.house.setValue("");
        }
    }
}
