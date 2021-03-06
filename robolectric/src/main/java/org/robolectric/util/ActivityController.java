package org.robolectric.util;

import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import org.robolectric.AndroidManifest;
import org.robolectric.RoboInstrumentation;
import org.robolectric.Robolectric;
import org.robolectric.res.ResName;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowActivityThread;
import org.robolectric.shadows.ShadowApplication;

import static org.robolectric.Shadows.shadowOf_;

public class ActivityController<T extends Activity>
    extends ComponentController<ActivityController<T>, T, ShadowActivity> {

  public static <T extends Activity> ActivityController<T> of(Class<T> activityClass) {
    return new ActivityController<T>(ReflectionHelpers.<T>callConstructorReflectively(activityClass));
  }

  public static <T extends Activity> ActivityController<T> of(T activity) {
    return new ActivityController<T>(activity);
  }

  public ActivityController(T activity) {
    super(activity);
  }

  public ActivityController<T> attach() {
    Application application = this.application == null ? Robolectric.application : this.application;
    Context baseContext = this.baseContext == null ? application : this.baseContext;
    Intent intent = getIntent();
    ActivityInfo activityInfo = new ActivityInfo();
    String activityTitle = getActivityTitle();

    ClassLoader cl = baseContext.getClassLoader();
    Class<?> activityThreadClass = null;
    try {
      activityThreadClass = cl.loadClass(ShadowActivityThread.CLASS_NAME);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
    Class<?> nonConfigurationInstancesClass = null;
    try {
      nonConfigurationInstancesClass = cl.loadClass("android.app.Activity$NonConfigurationInstances");
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }

    ReflectionHelpers.callInstanceMethodReflectively(component, "attach",
        new ReflectionHelpers.ClassParameter(Context.class, baseContext),
        new ReflectionHelpers.ClassParameter(activityThreadClass, null),
        new ReflectionHelpers.ClassParameter(Instrumentation.class, new RoboInstrumentation()),
        new ReflectionHelpers.ClassParameter(IBinder.class, null),
        new ReflectionHelpers.ClassParameter(int.class, 0),
        new ReflectionHelpers.ClassParameter(Application.class, application),
        new ReflectionHelpers.ClassParameter(Intent.class, intent),
        new ReflectionHelpers.ClassParameter(ActivityInfo.class, activityInfo),
        new ReflectionHelpers.ClassParameter(CharSequence.class, activityTitle),
        new ReflectionHelpers.ClassParameter(Activity.class, null),
        new ReflectionHelpers.ClassParameter(String.class, "id"),
        new ReflectionHelpers.ClassParameter(nonConfigurationInstancesClass, null),
        new ReflectionHelpers.ClassParameter(Configuration.class, application.getResources().getConfiguration()));

    shadow.setThemeFromManifest();
    attached = true;
    return this;
  }

  private String getActivityTitle() {
    String title = null;

    /* Get the label for the activity from the manifest */
    ShadowApplication shadowApplication = shadowOf_(component.getApplication());
    AndroidManifest appManifest = shadowApplication.getAppManifest();
    if (appManifest == null) return null;
    String labelRef = appManifest.getActivityLabel(component.getClass());

    if (labelRef != null) {
      if (labelRef.startsWith("@")) {
        /* Label refers to a string value, get the resource identifier */
        ResName style = ResName.qualifyResName(labelRef.replace("@", ""), appManifest.getPackageName(), "string");
        Integer labelRes = shadowApplication.getResourceLoader().getResourceIndex().getResourceId(style);

        /* If we couldn't determine the resource ID, throw it up */
        if (labelRes == null) {
          throw new Resources.NotFoundException("no such label " + style.getFullyQualifiedName());
        }

        /* Get the resource ID, use the activity to look up the actual string */
        title = component.getString(labelRes);
      } else {
        title = labelRef; /* Label isn't an identifier, use it directly as the title */
      }
    }

    return title;
  }

  public ActivityController<T> create(final Bundle bundle) {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        if (!attached) attach();
        ReflectionHelpers.callInstanceMethodReflectively(component, "performCreate", new ReflectionHelpers.ClassParameter(Bundle.class, bundle));
      }
    });
    return this;
  }

  public ActivityController<T> create() {
    return create(null);
  }

  public ActivityController<T> restoreInstanceState(Bundle bundle) {
    invokeWhilePaused("performRestoreInstanceState", bundle);
    return this;
  }

  public ActivityController<T> postCreate(Bundle bundle) {
    invokeWhilePaused("onPostCreate", bundle);
    return this;
  }

  public ActivityController<T> start() {
    invokeWhilePaused("performStart");
    return this;
  }

  public ActivityController<T> restart() {
    invokeWhilePaused("performRestart");
    return this;
  }

  public ActivityController<T> resume() {
    invokeWhilePaused("performResume");
    return this;
  }

  public ActivityController<T> postResume() {
    invokeWhilePaused("onPostResume");
    return this;
  }

  public ActivityController<T> newIntent(Intent intent) {
    invokeWhilePaused("onNewIntent", intent);
    return this;
  }

  public ActivityController<T> saveInstanceState(Bundle outState) {
    invokeWhilePaused("performSaveInstanceState", outState);
    return this;
  }

  public ActivityController<T> visible() {
    shadowMainLooper.runPaused(new Runnable() {
      @Override
      public void run() {
        ReflectionHelpers.setFieldReflectively(component, "mDecor", component.getWindow().getDecorView());
        ReflectionHelpers.callInstanceMethodReflectively(component, "makeVisible");
      }
    });

    return this;
  }

  public ActivityController<T> pause() {
    invokeWhilePaused("performPause");
    return this;
  }

  public ActivityController<T> userLeaving() {
    invokeWhilePaused("performUserLeaving");
    return this;
  }

  public ActivityController<T> stop() {
    invokeWhilePaused("performStop");
    return this;
  }

  public ActivityController<T> destroy() {
    invokeWhilePaused("performDestroy");
    return this;
  }

  /**
   * Calls the same lifecycle methods on the Activity called by
   * Android the first time the Activity is created.
   */
  public ActivityController<T> setup() {
    return create().start().postCreate(null).resume().visible();
  }

  /**
   * Calls the same lifecycle methods on the Activity called by
   * Android when an Activity is restored from previously saved state.
   */
  public ActivityController<T> setup(Bundle savedInstanceState) {
    return create(savedInstanceState)
        .start()
        .restoreInstanceState(savedInstanceState)
        .postCreate(savedInstanceState)
        .resume()
        .visible();
  }
}
