package com.androidsx.rateme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidsx.libraryrateme.R;
import com.androidsx.rateme.DialogRateMe.Builder;
import com.androidsx.rateme.DialogRateMe.RateMeAction;

public class DialogRateMe {
    private static final String TAG = DialogRateMe.class.getSimpleName();

    private static final String MARKET_CONSTANT = "market://details?id=";
    private static final String GOOGLE_PLAY_CONSTANT = "http://play.google.com/store/apps/details?id=";

    // Identify TitleDivider
    private String RESOURCE_NAME = "titleDivider";
    private String DEFAULT_TYPE_RESOURCE = "id";
    private String DEFAULT_PACKAGE = "android";

    // Views
    private View mView;
    private View tView;
    private Button close;
    private RatingBar ratingBar;
    private LayerDrawable stars;
    private Button rateMe;
    private Button noThanks;
    private Button share;

    // configuration
    private final String appPackageName;
    private final String appName;
    private final boolean goToMail;
    private final String email;
    private final boolean showShareButton;
    private final int titleTextColor;
    private final int titleBackgroundColor;
    private final int dialogColor;
    private final int lineDividerColor;
    private final int textColor;
    private final int logoResId;
    private final int rateButtonBackgroundColor;
    private final int rateButtonTextColor;
    private final int rateButtonPressedBackgroundColor;
    private final int defaultStarsSelected;
    private final int iconCloseColor;
    private final int iconShareColor;
    private final boolean showOKButtonByDefault;
    private final RateMeOnActionListener onActionListener;
    private Context context;
    private AlertDialog dialog;

    private DialogRateMe(Builder builder) {
        this.appPackageName = builder.appPackageName;
        this.appName = builder.appName;
        this.goToMail = builder.goToMail;
        this.email = builder.email;
        this.showShareButton = builder.showShareButton;
        this.titleTextColor = builder.titleTextColor;
        this.titleBackgroundColor = builder.titleBackgroundColor;
        this.dialogColor = builder.dialogColor;
        this.lineDividerColor = builder.lineDividerColor;
        this.textColor = builder.textColor;
        this.logoResId = builder.logoResId;
        this.rateButtonBackgroundColor = builder.rateButtonBackgroundColor;
        this.rateButtonTextColor = builder.rateButtonTextColor;
        this.rateButtonPressedBackgroundColor = builder.rateButtonPressedBackgroundColor;
        this.defaultStarsSelected = builder.defaultStarsSelected;
        this.iconCloseColor = builder.iconCloseColor;
        this.iconShareColor = builder.iconShareColor;
        this.showOKButtonByDefault = builder.showOKButtonByDefault;
        this.onActionListener = builder.onActionListener;
        this.context = builder.context;
    }
    
    public void Show(){
        dialog = onCreateDialog(null);
        onStart();
    	//dialog.show();
    }

    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        initializeUiFields();
        Log.d(TAG, "initialize correctly all the components");

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        setIconsTitleColor(iconCloseColor, iconShareColor);

        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        if (!showOKButtonByDefault) {
            ratingBar.setRating((float) defaultStarsSelected);
        }
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                /*
            	if (rating >= 4.0) {
                    rateMe.setVisibility(View.VISIBLE);
                    noThanks.setVisibility(View.GONE);
                } else {
                    noThanks.setVisibility(View.VISIBLE);
                    rateMe.setVisibility(View.GONE);
                }
                */
            }
        });
        ratingBar.setRating((float) defaultStarsSelected);
        configureButtons();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	dialog.dismiss();
                RateMeDialogTimer.clearSharedPreferences(context);
                Log.d(TAG, "clear preferences");
                RateMeDialogTimer.setOptOut(context, true);
                onActionListener.onActionPerformed(RateMeAction.DISMISSED_WITH_CROSS, ratingBar.getRating());
            }
        });
        share.setVisibility(showShareButton ? View.VISIBLE : View.GONE);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(shareApp(appPackageName));
                Log.d(TAG, "share App");
                onActionListener.onActionPerformed(RateMeAction.SHARED_APP, ratingBar.getRating());
            }
        });
        return builder.setView(mView).setCustomTitle(tView).setCancelable(false).show();
    }

    public void onStart() {
        final int titleDividerId = context.getResources().getIdentifier(RESOURCE_NAME, DEFAULT_TYPE_RESOURCE, DEFAULT_PACKAGE);
        final View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(lineDividerColor);
        }
    }

    private void initializeUiFields() {
        // Main Dialog
        GradientDrawable rateMeButton = (GradientDrawable) context.getResources().getDrawable(R.drawable.customshape);
        GradientDrawable rateMeButtonItemSelected = (GradientDrawable) context.getResources().getDrawable(
                R.drawable.itemselected);
       
        rateMeButton.setColor(rateButtonBackgroundColor);
        rateMeButtonItemSelected.setColor(rateButtonPressedBackgroundColor);
        mView = ((Activity)context).getLayoutInflater().inflate(R.layout.library, null);
        tView = ((Activity)context).getLayoutInflater().inflate(R.layout.title, null);
        close = (Button) tView.findViewById(R.id.buttonClose);
        share = (Button) tView.findViewById(R.id.buttonShare);
        rateMe = (Button) mView.findViewById(R.id.buttonRateMe);
        noThanks = (Button) mView.findViewById(R.id.buttonThanks);
        ratingBar = (RatingBar) mView.findViewById(R.id.ratingBar);
        stars = (LayerDrawable) ratingBar.getProgressDrawable();
        mView.setBackgroundColor(dialogColor);
        tView.setBackgroundColor(titleBackgroundColor);
        ((TextView) tView.findViewById(R.id.title)).setTextColor(titleTextColor);
        ((ImageView) mView.findViewById(R.id.picture)).setImageResource(logoResId);
        ((TextView) mView.findViewById(R.id.phraseCenter)).setTextColor(textColor);
        rateMe.setTextColor(rateButtonTextColor);
        noThanks.setTextColor(rateButtonTextColor);

    }

    private void configureButtons() {
        rateMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
                Log.d(TAG, "go to Google Play Store for Rate-Me");
                RateMeDialogTimer.setOptOut(context, true);
                onActionListener.onActionPerformed(RateMeAction.HIGH_RATING_WENT_TO_GOOGLE_PLAY, ratingBar.getRating());
                dialog.dismiss();
            }
        });

        noThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goToMail) {
                	DialogGoToMail dialogMail = DialogGoToMail.newInstance(context, appName, email, dialogColor, textColor, logoResId, rateButtonTextColor, lineDividerColor, ratingBar.getRating() );
                    dialogMail.Show();
                    dialog.dismiss();
                    Log.d(TAG, "got to Mail for explain what is the problem");
                } else {
                    dialog.dismiss();
                    onActionListener.onActionPerformed(RateMeAction.LOW_RATING, ratingBar.getRating());
                }
                RateMeDialogTimer.setOptOut(context, true);
            }
        });
        

        noThanks.setVisibility(View.VISIBLE);
        rateMe.setVisibility(View.GONE);
    }

    private void rateApp() {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_CONSTANT + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(GOOGLE_PLAY_CONSTANT + appPackageName)));
        }
    }

    private Intent shareApp(String appPackageName) {
        Intent shareApp = new Intent();
        shareApp.setAction(Intent.ACTION_SEND);
        try {
            shareApp.putExtra(Intent.EXTRA_TEXT, MARKET_CONSTANT + appPackageName);
        } catch (android.content.ActivityNotFoundException anfe) {
            shareApp.putExtra(Intent.EXTRA_TEXT, GOOGLE_PLAY_CONSTANT + appPackageName);
        }
        shareApp.setType("text/plain");
        return shareApp;
    }

    private void setIconsTitleColor(int colorClose, int colorShare) {
        Drawable iconClose = context.getResources().getDrawable(R.drawable.ic_action_cancel);
        Drawable iconShare = context.getResources().getDrawable(R.drawable.ic_action_share);
        ColorFilter filterIconClose = new LightingColorFilter(colorClose, colorClose);
        ColorFilter filterIconShare = new LightingColorFilter(colorShare, colorShare);
        iconClose.setColorFilter(filterIconClose);
        iconShare.setColorFilter(filterIconShare);
    }

    /**
     * Listener for the final action that the user takes.
     */
    public interface RateMeOnActionListener {
        void onActionPerformed(RateMeAction action, float rating);
    }

    /**
     * Different actions that the user can take.
     */
    public enum RateMeAction {
        /** We took them to Google Play. Typically after a good rating. */
        HIGH_RATING_WENT_TO_GOOGLE_PLAY,

        /** After a negative rating, he accepted to give us some feedback. */
        LOW_RATING_GAVE_FEEDBACK,

        /** After a negative rating, he didn't give us some feedback. */
        LOW_RATING_REFUSED_TO_GIVE_FEEDBACK,

        /**
         * Gave a negative rating. Providing feedback is not configured, so the user gave the rating
         * and left.
         */
        LOW_RATING,

        /**
         * Dismissed the dialog with the cross in the upper-right corner. Note that we do NOT track
         * if the user dismisses the dialog through the back button.
         */
        DISMISSED_WITH_CROSS,

        /** Shared the link to the app through the button in the top-right corner. */
        SHARED_APP;
    }

    public static class Builder {
        private String appPackageName;
        private String appName;
        private boolean goToMail;
        private String email;
        private boolean showShareButton;
        private int titleTextColor = Color.WHITE;
        private int titleBackgroundColor = Color.BLACK;
        private int dialogColor = Color.DKGRAY;
        private int lineDividerColor = dialogColor;
        private int textColor = Color.WHITE;
        private int logoResId = R.drawable.ic_launcher;
        private int rateButtonBackgroundColor = Color.BLACK;
        private int rateButtonTextColor = Color.WHITE;
        private int rateButtonPressedBackgroundColor = Color.GRAY;
        private int defaultStarsSelected = 0;
        private int iconCloseColor = Color.WHITE;
        private int iconShareColor = Color.WHITE;
        private boolean showOKButtonByDefault = true;
        
        private Context context;

        /**
         * Default implementation for the action listener, that just logs every action.
         */
        private RateMeOnActionListener onActionListener = new RateMeOnActionListener() {
            @Override
            public void onActionPerformed(RateMeAction action, float rating) {
                Log.d(TAG, "Action " + action + " (rating: " + rating + ")");
            }
        };

        public Builder(Context ctx) {
        	this.context = ctx;
            this.appPackageName = ctx.getApplicationContext().getPackageName();
        }

        public Builder(DialogGoToMail dialogGoToMail) {
            return;
        }

        /**
         * Set the company email when select {@code true} in {@link #setGoToMail(boolean)}
         * if you select {@code false}, no need to configure
         * @param email String variable is a email of company
         * @return
         */
        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setShowShareButton(boolean showShareButton) {
            this.showShareButton = showShareButton;
            return this;
        }
        
        /**
         * Set if you want to display a new dialog when the user selects less than 4 star. On this
         * dialog the user may send feedback for your App
         * If you select {@code true} you must set the mail with {@link #setEmail(String)}, if not the App
         * will closed by exception : IllegalArgumentException
         *
         * @param goToMail boolean variable to see new dialog for get feedback in your App
         * @return this builder
         */
        public Builder setGoToMail(boolean goToMail) {
            this.goToMail = goToMail;
            return this;
        }
        
        public Builder setAppName(String appName) {
            this.appName = appName;
            return this;
        }

        public Builder setTitleTextColor(int titleTextColor) {
            this.titleTextColor = titleTextColor;
            return this;
        }

        public Builder setTitleBackgroundColor(int titleBackgroundColor) {
            this.titleBackgroundColor = titleBackgroundColor;
            return this;
        }

        public Builder setDialogColor(int dialogColor) {
            this.dialogColor = dialogColor;
            return this;
        }

        public Builder setLineDividerColor(int lineDividerColor) {
            this.lineDividerColor = lineDividerColor;
            return this;
        }

        /** Set a icon that will take the dialog. If not you set any icon, the icon that will
         * put by default is {@code ic_launcher}
         *
         * @param logoResId App icon
         * @return this builder
         */
        public Builder setLogoResourceId(int logoResId) {
            this.logoResId = logoResId;
            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder setRateButtonBackgroundColor(int rateButtonBackgroundColor) {
            this.rateButtonBackgroundColor = rateButtonBackgroundColor;
            return this;
        }

        public Builder setRateButtonTextColor(int rateButtonTextColor) {
            this.rateButtonTextColor = rateButtonTextColor;
            return this;
        }

        public Builder setRateButtonPressedBackgroundColor(int rateButtonPressedBackgroundColor) {
            this.rateButtonPressedBackgroundColor = rateButtonPressedBackgroundColor;
            return this;
        }

        public Builder setDefaultStartSelected(int numStars) {
            this.defaultStarsSelected = numStars;
            return this;
        }

        public Builder setIconCloseColorFilter(int iconColor) {
            this.iconCloseColor = iconColor;
            return this;
        }

        public Builder setIconShareColorFilter(int iconColor) {
            this.iconShareColor = iconColor;
            return this;
        }

        public Builder setShowOKButtonByDefault(boolean visible) {
            this.showOKButtonByDefault = visible;
            return this;
        }

        /**
         * Sets a listener that will get notified after the action executes the final action in the
         * dialog, such as rating the app or deciding to leave some feedback. Typically you want to
         * track this to have some usage statistics.
         *
         * @param onActionListener listener for the final user action
         * @return this builder
         */
        public Builder setOnActionListener(RateMeOnActionListener onActionListener) {
            this.onActionListener = onActionListener;
            return this;
        }

        public RateMeOnActionListener getOnActionListener() {
            return onActionListener;
        }

        public DialogRateMe build() {
            if (goToMail && email == null) {
                throw new IllegalArgumentException("You Have to configure the email for the dialog goToMail");
            }
            return new DialogRateMe(this);
        }
        
    }

}

class DialogGoToMail {
    private static final String TAG = DialogGoToMail.class.getSimpleName();
    
    private static final String EXTRA_EMAIL = "email";
    private static final String EXTRA_APP_NAME = "app-name";
    private static final String EXTRA_DIALOG_COLOR = "dialog-color";
    private static final String EXTRA_TEXT_COLOR = "text-color";
    private static final String EXTRA_LOGO = "icon";
    private static final String EXTRA_RATE_BUTTON_TEXT_COLOR = "button-text-color";
    private static final String EXTRA_TITLE_DIVIDER = "color-title-divider";
    private static final String EXTRA_RATING_BAR = "get-rating";
    
    // Views
    private View confirDialogTitleView;
    private View confirDialogView;
    private Button cancel;
    private Button yes;
    
    private Bundle args;
    private Context context;
    private AlertDialog dialog;
    
    Builder rateMe = new DialogRateMe.Builder(this);
    
    public static DialogGoToMail newInstance (Context context, String appName, String email, int dialogColor, int textColor,int logoResId, int rateButtonTextColor,int lineDividerColor, float getRatingBar){
        DialogGoToMail dialogo = new DialogGoToMail();
        dialogo.args = new Bundle();
        dialogo.args.putString(EXTRA_EMAIL, email);
        dialogo.args.putString(EXTRA_APP_NAME, appName);
        dialogo.args.putInt(EXTRA_DIALOG_COLOR, dialogColor);
        dialogo.args.putInt(EXTRA_TEXT_COLOR, textColor);        
        dialogo.args.putInt(EXTRA_LOGO, logoResId);
        dialogo.args.putInt(EXTRA_RATE_BUTTON_TEXT_COLOR, rateButtonTextColor);
        dialogo.args.putInt(EXTRA_TITLE_DIVIDER, lineDividerColor);
        dialogo.args.putFloat(EXTRA_RATING_BAR, getRatingBar);
        //dialogo.setArguments(args);
        dialogo.context = context;
        return dialogo;
        
    }
    
    public void Show(){
        dialog = onCreateDialog(null);
        onStart();
    	//dialog.show();
    }
    
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        initializeUiFieldsDialogGoToMail();
        Log.d(TAG, "initialize correctly all the components");
        
        cancel.setOnClickListener(new View.OnClickListener()  {
            public void onClick(View v) {
            	dialog.dismiss();
                rateMe.getOnActionListener().onActionPerformed(RateMeAction.LOW_RATING_REFUSED_TO_GIVE_FEEDBACK, args.getFloat(EXTRA_RATING_BAR));
                Log.d(TAG, "Close dialog Mail");
            }
        });  
        
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMail(args.getFloat(EXTRA_RATING_BAR), args.getString(EXTRA_APP_NAME));
                rateMe.getOnActionListener().onActionPerformed(RateMeAction.LOW_RATING_GAVE_FEEDBACK, args.getFloat(EXTRA_RATING_BAR));
                Log.d(TAG, "Go to mail");
                dialog.dismiss();
            }
        });
        
        return builder.setCustomTitle(confirDialogTitleView).setView(confirDialogView).show();
    }
    
    private void initializeUiFieldsDialogGoToMail(){
        confirDialogTitleView = ((Activity)context).getLayoutInflater().inflate(R.layout.gotomail_dialog_title, null);
        confirDialogView = ((Activity)context).getLayoutInflater().inflate(R.layout.gotomail_dialog_body, null);
        confirDialogTitleView.setBackgroundColor(args.getInt(EXTRA_DIALOG_COLOR));
        confirDialogView.setBackgroundColor(args.getInt(EXTRA_DIALOG_COLOR));
        ((ImageView) confirDialogView.findViewById(R.id.icon)).setImageResource(args.getInt(EXTRA_LOGO));
        ((TextView) confirDialogTitleView.findViewById(R.id.confirmDialogTitle)).setTextColor(args.getInt(EXTRA_TEXT_COLOR));
        ((TextView) confirDialogView.findViewById(R.id.phraseMail)).setTextColor(args.getInt(EXTRA_TEXT_COLOR));
        cancel = (Button) confirDialogView.findViewById(R.id.buttonCancel);
        yes = (Button) confirDialogView.findViewById(R.id.buttonYes);
        cancel.setTextColor(args.getInt(EXTRA_RATE_BUTTON_TEXT_COLOR));
        yes.setTextColor(args.getInt(EXTRA_RATE_BUTTON_TEXT_COLOR));
    }
    
    private void goToMail(float rating, String appName) {
        final String subject = context.getResources().getString(R.string.subject_email)+" "+appName;
        try {
            Intent sendMailtoGmail = new Intent(Intent.ACTION_SEND);
            sendMailtoGmail.setType("plain/text");
            sendMailtoGmail.putExtra(Intent.EXTRA_EMAIL, new String[] { args.getString(EXTRA_EMAIL) });
            sendMailtoGmail.putExtra(Intent.EXTRA_SUBJECT, subject);
            sendMailtoGmail.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("i give this app rating : "+rating+"<br/>reason?"));
			//email.setType("message/rfc822");
			
            sendMailtoGmail.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
            context.startActivity(Intent.createChooser(sendMailtoGmail, ""));
            if(2+2==4){
                throw new ActivityNotFoundException("excepcion de prueba"); 
            }
            
            
        } catch (android.content.ActivityNotFoundException ex) {
            Log.w(TAG, "Cannot send email with Gmail, use the generic chooser");
            Intent sendGeneric = new Intent(Intent.ACTION_SEND);
            sendGeneric.setType("plain/text");
            sendGeneric.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("i give this app rating : "+rating+"<br/>reason?"));
            sendGeneric.putExtra(Intent.EXTRA_EMAIL, new String[] { args.getString(EXTRA_EMAIL) });
            sendGeneric.putExtra(Intent.EXTRA_SUBJECT, subject);
            context.startActivity(Intent.createChooser(sendGeneric, ""));
        }
    }
    
    public void onStart() {
        final int titleDividerId = context.getResources().getIdentifier("titleDivider", "id", "android");
        final View titleDivider = dialog.findViewById(titleDividerId);
        if (titleDivider != null) {
            titleDivider.setBackgroundColor(args.getInt(EXTRA_TITLE_DIVIDER));
        }
    }
    
}
