/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.demo.ui.shared;

import java.util.concurrent.TimeUnit;

import io.github.mmm.base.placement.Direction;
import io.github.mmm.ui.UiContext;
import io.github.mmm.ui.binding.data.UiDataBinding;
import io.github.mmm.ui.datatype.chart.UiDataSeriesDouble;
import io.github.mmm.ui.datatype.chart.UiDataSet;
import io.github.mmm.ui.datatype.media.UiMedia;
import io.github.mmm.ui.datatype.media.UiMediaSource;
import io.github.mmm.ui.datatype.media.UiMediaTrack;
import io.github.mmm.ui.event.UiValueChangeEventListener;
import io.github.mmm.ui.widget.UiLabel;
import io.github.mmm.ui.widget.attribute.UiWidgetWithAutocomplete;
import io.github.mmm.ui.widget.button.UiButton;
import io.github.mmm.ui.widget.chart.UiBarChartHorizontal;
import io.github.mmm.ui.widget.chart.UiPieChart;
import io.github.mmm.ui.widget.composite.UiTab;
import io.github.mmm.ui.widget.input.UiCheckbox;
import io.github.mmm.ui.widget.input.UiIntegerSlider;
import io.github.mmm.ui.widget.input.UiPasswordInput;
import io.github.mmm.ui.widget.input.UiRadioChoice;
import io.github.mmm.ui.widget.input.UiTextArea;
import io.github.mmm.ui.widget.input.UiTextInput;
import io.github.mmm.ui.widget.media.UiMediaPlayer;
import io.github.mmm.ui.widget.menu.UiMenu;
import io.github.mmm.ui.widget.menu.UiMenuBar;
import io.github.mmm.ui.widget.panel.UiButtonPanel;
import io.github.mmm.ui.widget.panel.UiFormGroup;
import io.github.mmm.ui.widget.panel.UiFormPanel;
import io.github.mmm.ui.widget.panel.UiTabPanel;
import io.github.mmm.ui.widget.panel.UiVerticalPanel;
import io.github.mmm.ui.widget.window.UiMainWindow;

/**
 * Demo of portable User-Interface with {@code mmm-ui}.
 */
@SuppressWarnings("unchecked")
public class DemoUi {

  private final UiContext context;

  /**
   * The constructor.
   */
  public DemoUi(UiContext context) {

    super();
    this.context = context;
  }

  public void run() {

    UiDataBinding binding = new UiDataBinding(this.context);
    UiMainWindow mainWindow = this.context.getMainWindow();
    initMenuBar(mainWindow);
    UiTabPanel tabPanel = UiTabPanel.of(this.context);
    UiTab tab1 = createTab1(tabPanel);
    UiVerticalPanel page2 = UiVerticalPanel.of(this.context);
    // UiTab tab2 =
    tabPanel.addChild(page2, "Tab2");
    UiTab tab3 = createDynamicEditor(binding, tabPanel);

    UiCheckbox showTab1 = UiCheckbox.of(this.context, "Show Tab1");
    showTab1.setId("showTab1");
    showTab1.setSelected(true);
    UiValueChangeEventListener showTab1Listener = (e) -> {
      boolean selected = showTab1.isSelected();
      tab1.setVisible(selected);
    };
    showTab1.addListener(showTab1Listener);
    UiCheckbox showTab3 = UiCheckbox.of(this.context, "Show Tab3");
    showTab3.setId("showTab3");
    showTab3.setSelected(true);
    UiValueChangeEventListener showTab3Listener = (e) -> {
      tab3.setVisible(showTab3.isSelected());
    };
    showTab3.addListener(showTab3Listener);
    UiTextInput textInput = UiTextInput.of(this.context, "Login");
    textInput.setValidator(ValidatorMandatory.getInstance());
    UiPasswordInput passwordInput = UiPasswordInput.ofNew(this.context, "Password");
    UiPasswordInput confirmPasswordInput;
    // confirmPasswordInput = passwordInput.createConfirmationInput();
    confirmPasswordInput = UiPasswordInput.of(this.context, "Confirm " + passwordInput.getName(),
        UiWidgetWithAutocomplete.AUTOCOMPLETE_NEW_PASSWORD);
    confirmPasswordInput.setValidator(new ValidatorPasswordConfirmation(() -> passwordInput.getValue()));

    UiTextArea textArea = UiTextArea.of(this.context, "Comment");
    UiRadioChoice<TimeUnit> choice = UiRadioChoice.ofEnum(this.context, "Time-unit", TimeUnit.class);
    UiIntegerSlider slider = UiIntegerSlider.of(this.context, "Slider");
    slider.setTextEditable(true);
    UiFormGroup<Void> formGroupTabs = UiFormGroup.of(this.context, "Show Tabs", showTab1, showTab3);
    UiFormGroup<Void> formGroupInputs = UiFormGroup.of(this.context, "Generic Inputs", textInput, passwordInput,
        confirmPasswordInput, textArea, choice, slider);

    UiFormPanel<Void> formPanel = UiFormPanel.of(this.context, formGroupTabs, formGroupInputs);
    page2.addChild(formPanel);
    UiButton submitButton = UiButton.of(this.context, "Submit", (e) -> {
      formPanel.validate();
    });
    submitButton.getStyles().add("submit");
    UiButton deleteButton = UiButton.of(this.context, "Delete", (e) -> {
      System.out.println("Delete");
    });
    deleteButton.getStyles().add("danger");
    UiButton cancelButton = UiButton.of(this.context, "Cancel", (e) -> {
      System.out.println("Cancel");
    });
    cancelButton.getStyles().add("abort");
    UiButtonPanel buttonPanel = UiButtonPanel.of(this.context, submitButton, deleteButton, cancelButton);
    page2.addChild(buttonPanel);
    createChartTab(tabPanel);
    createVideoTab(tabPanel);
    mainWindow.addChild(tabPanel);
    mainWindow.setVisible(true);
  }

  private UiTab createTab1(UiTabPanel tabPanel) {

    UiVerticalPanel page1 = UiVerticalPanel.of(this.context);
    UiTab tab1 = tabPanel.addChild(page1, "Tab1");
    page1.addChild(UiLabel.of(this.context, "Hello World"));
    page1.addChild(UiButton.of(this.context, "OK", (e) -> {
      System.out.println("OK");
    }));
    return tab1;
  }

  private UiTab createDynamicEditor(UiDataBinding binding, UiTabPanel tabPanel) {

    UiVerticalPanel page3 = UiVerticalPanel.of(this.context);
    UiFormPanel<TestBean> formPanel = binding.createFormPanel(new TestBean());
    page3.addChild(formPanel);
    UiButton submitButton = UiButton.of(this.context, "Submit", (e) -> {
      System.out.println("Submit");
      boolean valid = formPanel.validate();
      System.out.println("Valid:" + valid);
      System.out.println(formPanel.getValue().toString());
    });
    submitButton.getStyles().add("submit");
    UiButtonPanel buttonPanel = UiButtonPanel.of(this.context, submitButton);
    page3.addChild(buttonPanel);
    UiTab tab3 = tabPanel.addChild(page3, "Tab3");
    return tab3;
  }

  private void initMenuBar(UiMainWindow mainWindow) {

    UiMenuBar menuBar = mainWindow.getMenuBar();
    UiMenu fileMenu = menuBar.addMenu("File");
    fileMenu.addMenuItem("Exit", (e) -> {
      mainWindow.setVisible(false);
    });
    UiMenu optionsMenu = menuBar.addMenu("Options");
    optionsMenu.addMenuItem("Theme", (e) -> {
      System.out.println("Selected Theme from Options menu");
    });
  }

  private static void createChartTab(UiTabPanel tabPanel) {

    UiContext context = tabPanel.getContext();
    UiVerticalPanel panel = UiVerticalPanel.of(context);
    UiPieChart pieChart = context.create(UiPieChart.class);
    pieChart.setTitle("Pie Chart Demo");
    pieChart.setLegendPlacement(Direction.RIGHT);
    pieChart.setData(UiDataSet.of("AWT", 1), UiDataSet.of("Swing", 5), UiDataSet.of("SWT/Eclipse", 4),
        UiDataSet.of("JavaFx", 5), UiDataSet.of("JSF", 3), UiDataSet.of("GWT", 1), UiDataSet.of("AngularJS", 2),
        UiDataSet.of("Angular", 5), UiDataSet.of("React", 3), UiDataSet.of("Vue", 2), UiDataSet.of("Ionic", 2),
        UiDataSet.of("Android", 6), UiDataSet.of("iOS", 4));
    panel.addChild(pieChart);
    UiBarChartHorizontal barChart = context.create(UiBarChartHorizontal.class);
    barChart.setTitle("Bar Chart Demo");
    barChart.setSeriesLabels("2018", "2019", "2020");
    barChart.setData(UiDataSet.of("Angular", new UiDataSeriesDouble(2200, 3000, 2900)),
        UiDataSet.of("React", new UiDataSeriesDouble(1200, 2100, 3000)),
        UiDataSet.of("Vue", new UiDataSeriesDouble(250, 600, 1000)));
    panel.addChild(barChart);
    UiTab tab = tabPanel.addChild(panel, "Chart");
  }

  private static void createVideoTab(UiTabPanel tabPanel) {

    UiContext context = tabPanel.getContext();
    UiVerticalPanel page = UiVerticalPanel.of(context);
    UiMediaPlayer player = context.create(UiMediaPlayer.class);
    UiMediaSource sourceLq = new UiMediaSource("https://cdn.plyr.io/static/demo/View_From_A_Blue_Moon_Trailer-576p.mp4",
        UiMediaSource.MIMETYPE_VIDEO_MP4);
    UiMediaSource sourceHq = new UiMediaSource(
        "https://cdn.plyr.io/static/demo/View_From_A_Blue_Moon_Trailer-1080p.mp4", UiMediaSource.MIMETYPE_VIDEO_MP4);
    UiMediaTrack track = new UiMediaTrack("https://cdn.plyr.io/static/demo/View_From_A_Blue_Moon_Trailer-HD.en.vtt",
        UiMediaTrack.KIND_CAPTIONS, "en", "English");
    player.setMedia(UiMedia.ofVideo(new UiMediaSource[] { sourceLq, sourceHq }, new UiMediaTrack[] { track },
        "https://cdn.plyr.io/static/demo/View_From_A_Blue_Moon_Trailer-HD.jpg"));
    page.addChild(player);
    UiTab tab = tabPanel.addChild(page, "Video");
  }

}