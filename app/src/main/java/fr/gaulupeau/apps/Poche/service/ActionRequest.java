package fr.gaulupeau.apps.Poche.service;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import wallabag.apiwrapper.WallabagService;

import fr.gaulupeau.apps.Poche.network.Updater;

import static fr.gaulupeau.apps.Poche.service.ParcelableUtils.readInteger;
import static fr.gaulupeau.apps.Poche.service.ParcelableUtils.readLong;
import static fr.gaulupeau.apps.Poche.service.ParcelableUtils.readString;
import static fr.gaulupeau.apps.Poche.service.ParcelableUtils.writeInteger;
import static fr.gaulupeau.apps.Poche.service.ParcelableUtils.writeLong;
import static fr.gaulupeau.apps.Poche.service.ParcelableUtils.writeString;

public class ActionRequest implements Parcelable {

    public enum Action {
        SYNC_QUEUE, UPDATE_ARTICLES, SWEEP_DELETED_ARTICLES, FETCH_IMAGES, DOWNLOAD_AS_FILE
    }

    public enum RequestType {
        AUTO, MANUAL, MANUAL_BY_OPERATION
    }

    public static final String ACTION_REQUEST = "wallabag.extra.action_request";

    private Action action;
    private RequestType requestType = RequestType.MANUAL;
    private Long operationID;

    private Integer articleID;
    private String extra;
    private Updater.UpdateType updateType;
    private WallabagService.ResponseFormat downloadFormat;

    private ActionRequest nextRequest;

    public static ActionRequest fromIntent(Intent intent) {
        return intent.getParcelableExtra(ACTION_REQUEST);
    }

    public ActionRequest(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Long getOperationID() {
        return operationID;
    }

    public void setOperationID(Long operationID) {
        this.operationID = operationID;
    }

    public Integer getArticleID() {
        return articleID;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Updater.UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Updater.UpdateType updateType) {
        this.updateType = updateType;
    }

    public WallabagService.ResponseFormat getDownloadFormat() {
        return downloadFormat;
    }

    public void setDownloadFormat(WallabagService.ResponseFormat downloadFormat) {
        this.downloadFormat = downloadFormat;
    }

    public ActionRequest getNextRequest() {
        return nextRequest;
    }

    public void setNextRequest(ActionRequest nextRequest) {
        this.nextRequest = nextRequest;
    }

// Parcelable implementation

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(action.ordinal());
        out.writeInt(requestType.ordinal());
        writeLong(operationID, out);

        writeInteger(articleID, out);
        writeString(extra, out);
        writeInteger(updateType != null ? updateType.ordinal() : null, out);
        writeInteger(downloadFormat != null ? downloadFormat.ordinal() : null, out);
        out.writeParcelable(nextRequest, 0);
    }

    private ActionRequest(Parcel in) {
        action = Action.values()[in.readInt()];
        requestType = RequestType.values()[in.readInt()];
        operationID = readLong(in);

        articleID = readInteger(in);
        extra = readString(in);
        Integer feedUpdateUpdateTypeInteger = readInteger(in);
        if(feedUpdateUpdateTypeInteger != null) {
            updateType = Updater.UpdateType.values()[feedUpdateUpdateTypeInteger];
        }
        Integer downloadFormatInteger = readInteger(in);
        if(downloadFormatInteger != null) {
            downloadFormat = WallabagService.ResponseFormat.values()[downloadFormatInteger];
        }
        nextRequest = in.readParcelable(getClass().getClassLoader());
    }

    public static final Parcelable.Creator<ActionRequest> CREATOR
            = new Parcelable.Creator<ActionRequest>() {
        @Override
        public ActionRequest createFromParcel(Parcel in) {
            return new ActionRequest(in);
        }

        @Override
        public ActionRequest[] newArray(int size) {
            return new ActionRequest[size];
        }
    };

}
