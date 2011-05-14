package com.cloudtasks.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.google.web.bindery.requestfactory.shared.Receiver;

import com.cloudtasks.shared.CloudTasksRequestFactory;
import com.cloudtasks.shared.TaskProxy;
import com.cloudtasks.shared.TaskRequest;

import java.util.List;

public class CloudTasks implements EntryPoint {

  private static final String BUTTON_STYLE = "send centerbtn";
  
  TaskProxy taskProxy;
  final CloudTasksRequestFactory taskRequestFactory = GWT.create(CloudTasksRequestFactory.class);

  public void onModuleLoad() {
    RootPanel.get().add(new CloudTasksWidget());
    FlowPanel panel = new FlowPanel();
    panel.getElement().setId("header");

    HorizontalPanel hp = new HorizontalPanel();
    hp.setSpacing(10);

    HorizontalPanel wrapper = new HorizontalPanel();
    wrapper.setWidth("100%");
    wrapper.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    wrapper.add(hp);

    Button createButton = new Button();
    createButton.setText("Create");
    createButton.getElement().setClassName(BUTTON_STYLE);
    hp.add(createButton);
    Button updateButton = new Button();
    updateButton.setText("Update");
    updateButton.getElement().setClassName(BUTTON_STYLE);
    hp.add(updateButton);
    Button queryButton = new Button();
    queryButton.setText("Query");
    queryButton.getElement().setClassName(BUTTON_STYLE);
    hp.add(queryButton);
    Button deleteButton = new Button();
    deleteButton.setText("Delete");
    deleteButton.getElement().setClassName(BUTTON_STYLE);
    hp.add(deleteButton);

    final EventBus eventBus = new SimpleEventBus();
    final MyRequestFactory requestFactory = GWT.create(MyRequestFactory.class);
    requestFactory.initialize(eventBus);
    taskRequestFactory.initialize(eventBus);

    createButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        create();
      }
    });

    updateButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        update(taskProxy);
      }
    });

    deleteButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        delete(taskProxy);
      }
    });

    queryButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        query();
      }
    });

    panel.add(wrapper);
    RootPanel.get().add(panel);
  }

  private void create() {
    TaskRequest request = taskRequestFactory.taskRequest();
    request.createTask().fire(new Receiver<TaskProxy>() {
      @Override
      public void onSuccess(TaskProxy task) {
        Window.alert("CREATE SUCCESS:(" + task.getId() + ")");
        taskProxy = task;
      }
    });
  }

  private void update(TaskProxy task) {
    TaskRequest request = taskRequestFactory.taskRequest();
    taskProxy = request.edit(taskProxy);
    taskProxy.setName(getTaskName());
    request.updateTask(task).fire(new Receiver<TaskProxy>() {
      @Override
      public void onSuccess(TaskProxy task) {
        Window.alert("UPDATE SUCCESS:(" + task.getId() + "): " + task.getName());
      }
    });
  }

  private void delete(TaskProxy taskProxy) {
    taskRequestFactory.taskRequest().deleteTask(taskProxy).fire(
        new Receiver<Void>() {
          @Override
          public void onSuccess(Void v) {
            Window.alert("DELETE SUCCESS");
          }
        });
  }

  private void query() {
    taskRequestFactory.taskRequest().queryTasks().fire(
        new Receiver<List<TaskProxy>>() {
          @Override
          public void onSuccess(List<TaskProxy> taskList) {
            String names = "\n";
            for (TaskProxy task : taskList) {
              names += " (" + task.getId() + "): " + task.getName() + "\n";
            }
            Window.alert("QUERY SUCCESS: Count[" + taskList.size()
                + "] Values:" + names);
          }
        });
  }

  static int taskNameCt = 0;

  static String taskNames[] = {
     "Use *really big* blimps for streetview",
      "Implement plans to increase the speed of light",
      "Talk to Sergey about renting his aircraft carrier",
      "Make final payment on orbital death ray", "Give.",
      "Design coast-to-coast high-speed escape tunnel",};

  static String getTaskName() {
    if (taskNameCt >= taskNames.length) {
      taskNameCt = 0;
    }
    return taskNames[taskNameCt++];
  }

}