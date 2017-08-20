package com.whiuk.philip.perfect.ui.messageview;


import com.whiuk.philip.perfect.mailstore.AttachmentViewInfo;


interface AttachmentViewCallback {
    void onViewAttachment(AttachmentViewInfo attachment);
    void onSaveAttachment(AttachmentViewInfo attachment);
    void onSaveAttachmentToUserProvidedDirectory(AttachmentViewInfo attachment);
}
