package org.robolectric.shadows;

import android.app.Notification;
import android.app.Notification.BigTextStyle;
import android.app.Notification.Builder;
import android.app.Notification.Style;
import android.app.PendingIntent;
import android.content.Context;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;

import static org.robolectric.Robolectric.directlyOn;
import static org.robolectric.Shadows.shadowOf;

@Implements(Notification.class)
public class ShadowNotification {
  private static final int MAX_ACTIONS = 3;

  private CharSequence contentTitle;
  private CharSequence contentText;
  private CharSequence ticker;
  private CharSequence contentInfo;
  private int smallIcon;
  private long when;
  private boolean ongoing;
  private boolean showWhen;

  private ArrayList<Notification.Action> actions = new ArrayList<Notification.Action>(MAX_ACTIONS);

  private Style style;
  private Progress progress;
  private boolean usesChronometer;
  
  public Notification getRealNotification() {
    return realNotification;
  }

  @RealObject
  Notification realNotification;

  private LatestEventInfo latestEventInfo;

  public void __constructor__(int icon, CharSequence tickerText, long when) {
    realNotification.icon = icon;
    realNotification.tickerText = tickerText;
    realNotification.when = when;
  }

  public CharSequence getContentTitle() {
    return contentTitle;
  }

  public CharSequence getContentText() {
    return contentText;
  }

  public boolean isOngoing() {
    return ongoing;
  }

  public int getSmallIcon() {
    return smallIcon;
  }

  public long getWhen() {
    return when;
  }

  public boolean isWhenShown() {
    return showWhen;
  }

  public Style getStyle() {
    return style;
  }

  public Progress getProgress() {
    return progress;
  }
  
  public boolean usesChronometer() {
    return usesChronometer;
  }
  
  public void setContentTitle(CharSequence contentTitle) {
    this.contentTitle = contentTitle;
  }

  public void setContentText(CharSequence contentText) {
    this.contentText = contentText;
  }

  public void setOngoing(boolean ongoing) {
    this.ongoing = ongoing;
  }

  public void setSmallIcon(int icon) {
    this.smallIcon = icon;
  }

  public void setWhen(long when) {
    this.when = when;
  }

  public void setShowWhen(boolean showWhen) {
    this.showWhen = showWhen;
  }

  public CharSequence getTicker() {
   return ticker;
  }

  public void setTicker(CharSequence ticker) {
   this.ticker = ticker;
  }

  public CharSequence getContentInfo() {
   return contentInfo;
  }

  public void setContentInfo(CharSequence contentInfo) {
   this.contentInfo = contentInfo;
  }

  public void setActions(ArrayList<Notification.Action> actions) {
    this.actions = actions;
  }

  public ArrayList<Notification.Action> getActions() {
    return actions;
  }

  public void setStyle(Style style) {
    this.style = style;
  }

  public void setProgress(Progress progress) {
    this.progress = progress;
  }
  
  public void setUsesChronometer(boolean usesChronometer) {
    this.usesChronometer = usesChronometer;
  }
  
  @Implementation
  public void setLatestEventInfo(Context context, CharSequence contentTitle,
                   CharSequence contentText, PendingIntent contentIntent) {
    latestEventInfo = new LatestEventInfo(contentTitle, contentText, contentIntent);
    realNotification.contentIntent = contentIntent;
  }

  public LatestEventInfo getLatestEventInfo() {
    return latestEventInfo;
  }

  public static class LatestEventInfo {
    private final CharSequence contentTitle;
    private final CharSequence contentText;
    private final PendingIntent contentIntent;

    private LatestEventInfo(CharSequence contentTitle, CharSequence contentText, PendingIntent contentIntent) {
      this.contentTitle = contentTitle;
      this.contentText = contentText;
      this.contentIntent = contentIntent;
    }

    public CharSequence getContentTitle() {
      return contentTitle;
    }

    public CharSequence getContentText() {
      return contentText;
    }

    public PendingIntent getContentIntent() {
      return contentIntent;
    }
  }

  public static class Progress {
    public final int max;
    public final int progress;
    public final boolean indeterminate;
    
    private Progress(int max, int progress, boolean indeterminate) {
      this.max = max;
      this.progress = progress;
      this.indeterminate = indeterminate;
    }
  }
  
  @Implements(Builder.class)
  public static class ShadowBuilder {

    @RealObject private Builder realBuilder;
    private CharSequence contentTitle;
    private CharSequence contentInfo;
    private CharSequence contentText;
    private CharSequence ticker;
    private int smallIcon;
    private boolean ongoing;
    private long when;
    private boolean showWhen = true;
    private ArrayList<Notification.Action> actions =
        new ArrayList<Notification.Action>(MAX_ACTIONS);
    private Style style;
    private Progress progress;
    private boolean usesChronometer;

    @Implementation
    public Notification build() {
      final Notification result = directlyOn(realBuilder, Builder.class, "build");
      populateShadow(result);
      return result;
    }
    
    @Implementation
    public Notification buildUnstyled() {
      final Notification result = directlyOn(realBuilder, Builder.class, "buildUnstyled");
      populateShadow(result);
      return result;
    }
    
    private void populateShadow(Notification result) {
      ShadowNotification s = shadowOf(result);
      s.setContentTitle(contentTitle);
      s.setContentText(contentText);
      s.setSmallIcon(smallIcon);
      s.setTicker(ticker);
      s.setWhen(when);
      s.setShowWhen(showWhen);
      s.setContentInfo(contentInfo);
      s.setActions(actions);
      s.setStyle(style);
      s.setProgress(progress);
      s.setUsesChronometer(usesChronometer);
      s.setOngoing(ongoing);
    }
    
    @Implementation
    public Builder setContentTitle(CharSequence contentTitle) {
      this.contentTitle = contentTitle;
      directlyOn(realBuilder, Builder.class, "setContentTitle", new ReflectionHelpers.ClassParameter(CharSequence.class, contentTitle));
      return realBuilder;
    }

    @Implementation
    public Builder setContentText(CharSequence text) {
      this.contentText = text;
      directlyOn(realBuilder, Builder.class, "setContentText", new ReflectionHelpers.ClassParameter(CharSequence.class, text));
      return realBuilder;
    }

    @Implementation
    public Builder setSmallIcon(int smallIcon) {
      this.smallIcon = smallIcon;
      directlyOn(realBuilder, Builder.class, "setSmallIcon", new ReflectionHelpers.ClassParameter(int.class, smallIcon));
      return realBuilder;
    }

    @Implementation
    public Builder setOngoing(boolean ongoing) {
      this.ongoing = ongoing;
      directlyOn(realBuilder, Builder.class, "setOngoing", new ReflectionHelpers.ClassParameter(boolean.class, ongoing));
      return realBuilder;
    }

    @Implementation
    public Builder setWhen(long when) {
      this.when = when;
      directlyOn(realBuilder, Builder.class, "setWhen", new ReflectionHelpers.ClassParameter(long.class, when));
      return realBuilder;
    }

    @Implementation
    public Builder setTicker(CharSequence ticker) {
      this.ticker = ticker;
      directlyOn(realBuilder, Builder.class, "setTicker", new ReflectionHelpers.ClassParameter(CharSequence.class, ticker));
      return realBuilder;
    }

    @Implementation
    public Builder setContentInfo(CharSequence contentInfo) {
      this.contentInfo = contentInfo;
      directlyOn(realBuilder, Builder.class, "setContentInfo", new ReflectionHelpers.ClassParameter(CharSequence.class, contentInfo));
      return realBuilder;
    }

    @Implementation
    public Builder addAction(int icon, CharSequence title, PendingIntent intent) {
      this.actions.add(new Notification.Action(icon, title, intent));
      // TODO: Call addAction on real builder after resolving issue with RemoteViews bitmap cache.
      // directlyOn(realBuilder, Builder.class, "addAction", int.class,
      //     CharSequence.class, PendingIntent.class).invoke(icon, title, intent);
      return realBuilder;
    }

    @Implementation
    public Builder setStyle(Style style) {
      this.style = style;
      directlyOn(realBuilder, Builder.class, "setStyle", new ReflectionHelpers.ClassParameter(Style.class, style));

      return realBuilder;
    }
    
    @Implementation
    public Builder setProgress(int max, int progress, boolean indeterminate) {
      this.progress = new Progress(max, progress, indeterminate);
      directlyOn(realBuilder, Builder.class, "setProgress",
          new ReflectionHelpers.ClassParameter(int.class, max), new ReflectionHelpers.ClassParameter(int.class, progress), new ReflectionHelpers.ClassParameter(boolean.class, indeterminate));

      return realBuilder;
    }

    @Implementation
    public Builder setUsesChronometer(boolean usesChronometer) {
      this.usesChronometer = usesChronometer;
      directlyOn(realBuilder, Builder.class, "setUsesChronometer", new ReflectionHelpers.ClassParameter(boolean.class, usesChronometer));
      return realBuilder;
    }

    @Implementation
    public Builder setShowWhen(boolean showWhen) {
      this.showWhen = showWhen;
      directlyOn(realBuilder, Builder.class, "setShowWhen", new ReflectionHelpers.ClassParameter(boolean.class, showWhen));
      return realBuilder;
    }
  }

  @Implements(Style.class)
  public static class ShadowStyle {

    @RealObject
    protected Style realStyle;

    private CharSequence bigContentTitle;
    private CharSequence summaryText;

    @Implementation
    public void internalSetBigContentTitle(CharSequence bigContentTitle) {
      this.bigContentTitle = bigContentTitle;
      directlyOn(realStyle, Style.class, "internalSetBigContentTitle", new ReflectionHelpers.ClassParameter(CharSequence.class, bigContentTitle));
    }
    
    @Implementation
    public void internalSetSummaryText(CharSequence summaryText) {
      this.summaryText = summaryText;
      directlyOn(realStyle, Style.class, "internalSetSummaryText", new ReflectionHelpers.ClassParameter(CharSequence.class, summaryText));
    }

    /**
     * Non-Android accessors
     */
    public CharSequence getBigContentTitle() {
      return bigContentTitle;
    }

    public CharSequence getSummaryText() {
      return summaryText;
    }
  }
  
  @Implements(BigTextStyle.class)
  public static class ShadowBigTextStyle extends ShadowStyle {

    @RealObject private BigTextStyle realStyle;
    private CharSequence bigText;
    
    @Implementation
    public BigTextStyle bigText(CharSequence bigText) {
      this.bigText = bigText;
      directlyOn(realStyle, BigTextStyle.class, "bigText", new ReflectionHelpers.ClassParameter(CharSequence.class, bigText));
      return realStyle;
    }
    
    public CharSequence getBigText() {
      return bigText;
    }
  }
}
