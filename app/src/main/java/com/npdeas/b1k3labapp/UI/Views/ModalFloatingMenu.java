package com.npdeas.b1k3labapp.UI.Views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;

import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.npdeas.b1k3labapp.Database.AppDatabase;
import com.npdeas.b1k3labapp.Database.Structs.User;
import com.npdeas.b1k3labapp.Database.UserTask;
import com.npdeas.b1k3labapp.R;
import com.npdeas.b1k3labapp.Route.Npdeas.ModalType;

import java.util.ArrayList;


//@CoordinatorLayout.DefaultBehavior(MoveUpwardBehaviour.class)
public class ModalFloatingMenu extends SpeedDialView implements SpeedDialView.OnActionSelectedListener {

    private Context context;
    private ModalType modal;

    public ModalFloatingMenu(Context context) {
        super(context);
    }

    public ModalFloatingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    private void init(Context context) {
        this.context = context;
        setOnActionSelectedListener(this);
        setMainFabClosedBackgroundColor(getResources().getColor(R.color.greenLightNpDeas));
        setMainFabOpenedBackgroundColor(getResources().getColor(R.color.greenLightNpDeas));
        modal = ModalType.BIKE;
        setMainFabClosedDrawable(getResources().getDrawable(R.drawable.ic_bike));

        int i = 0;

        for (ModalType modalType : ModalType.values()) {
            if (modalType.imgId != 0) {
                SpeedDialActionItem.Builder buttonBuilder =
                        new SpeedDialActionItem.Builder(modalType.value, modalType.imgId);
                buttonBuilder.setFabBackgroundColor(getResources().getColor(R.color.greenLightNpDeas));
                //button.setLabelText(buttonsLabel[i]);
                buttonBuilder.setLabel(modalType.name);
                SpeedDialActionItem button = buttonBuilder.create();
                addActionItem(button);
            }
            i++;
        }
    }

    @Override
    public boolean onActionSelected(SpeedDialActionItem actionItem) {
        setMainFabClosedDrawable(actionItem.getFabImageDrawable(context));
        modal = ModalType.getValue(actionItem.getId());
        User.getCurrentUser().modalType = modal;
        new UpdateTask().execute();
        close();
        return true;
    }

    public ModalType getModal() {
        return modal;
    }


    public void setModal(ModalType modal) {
        setMainFabClosedDrawable(context.getDrawable(modal.imgId));
        this.modal = modal;
    }

    class UpdateTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            AppDatabase.getAppDatabase(context).userDao().update(User.getCurrentUser());
            return null;
        }
    }


}
