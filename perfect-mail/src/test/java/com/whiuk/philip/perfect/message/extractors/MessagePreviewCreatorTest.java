package com.whiuk.philip.perfect.message.extractors;


import com.whiuk.philip.perfect.mail.Message;
import com.whiuk.philip.perfect.mail.Part;
import com.whiuk.philip.perfect.mail.internet.MimeMessage;
import com.whiuk.philip.perfect.message.extractors.PreviewResult.PreviewType;
import org.junit.Before;
import org.junit.Test;

import static com.whiuk.philip.perfect.message.MessageCreationHelper.createEmptyPart;
import static com.whiuk.philip.perfect.message.MessageCreationHelper.createTextPart;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;


public class MessagePreviewCreatorTest {
    private TextPartFinder textPartFinder;
    private PreviewTextExtractor previewTextExtractor;
    private EncryptionDetector encryptionDetector;
    private MessagePreviewCreator previewCreator;

    @Before
    public void setUp() throws Exception {
        textPartFinder = mock(TextPartFinder.class);
        previewTextExtractor = mock(PreviewTextExtractor.class);
        encryptionDetector = mock(EncryptionDetector.class);

        previewCreator = new MessagePreviewCreator(textPartFinder, previewTextExtractor, encryptionDetector);
    }

    @Test
    public void createPreview_withEncryptedMessage() throws Exception {
        Message message = createDummyMessage();
        when(encryptionDetector.isEncrypted(message)).thenReturn(true);

        PreviewResult result = previewCreator.createPreview(message);

        assertFalse(result.isPreviewTextAvailable());
        assertEquals(PreviewType.ENCRYPTED, result.getPreviewType());
        verifyNoMoreInteractions(textPartFinder);
        verifyNoMoreInteractions(previewTextExtractor);
    }

    @Test
    public void createPreview_withoutTextPart() throws Exception {
        Message message = createDummyMessage();
        when(encryptionDetector.isEncrypted(message)).thenReturn(false);
        when(textPartFinder.findFirstTextPart(message)).thenReturn(null);

        PreviewResult result = previewCreator.createPreview(message);

        assertFalse(result.isPreviewTextAvailable());
        assertEquals(PreviewType.NONE, result.getPreviewType());
        verifyNoMoreInteractions(previewTextExtractor);
    }

    @Test
    public void createPreview_withEmptyTextPart() throws Exception {
        Message message = createDummyMessage();
        Part textPart = createEmptyPart("text/plain");
        when(encryptionDetector.isEncrypted(message)).thenReturn(false);
        when(textPartFinder.findFirstTextPart(message)).thenReturn(textPart);

        PreviewResult result = previewCreator.createPreview(message);

        assertFalse(result.isPreviewTextAvailable());
        assertEquals(PreviewType.NONE, result.getPreviewType());
        verifyNoMoreInteractions(previewTextExtractor);
    }

    @Test
    public void createPreview_withTextPart() throws Exception {
        Message message = createDummyMessage();
        Part textPart = createTextPart("text/plain");
        when(encryptionDetector.isEncrypted(message)).thenReturn(false);
        when(textPartFinder.findFirstTextPart(message)).thenReturn(textPart);
        when(previewTextExtractor.extractPreview(textPart)).thenReturn("expected");

        PreviewResult result = previewCreator.createPreview(message);

        assertTrue(result.isPreviewTextAvailable());
        assertEquals(PreviewType.TEXT, result.getPreviewType());
        assertEquals("expected", result.getPreviewText());
    }

    @Test
    public void createPreview_withPreviewTextExtractorThrowing() throws Exception {
        Message message = createDummyMessage();
        Part textPart = createTextPart("text/plain");
        when(encryptionDetector.isEncrypted(message)).thenReturn(false);
        when(textPartFinder.findFirstTextPart(message)).thenReturn(textPart);
        when(previewTextExtractor.extractPreview(textPart)).thenThrow(new PreviewExtractionException(""));

        PreviewResult result = previewCreator.createPreview(message);

        assertFalse(result.isPreviewTextAvailable());
        assertEquals(PreviewType.ERROR, result.getPreviewType());
    }

    private Message createDummyMessage() {
        return new MimeMessage();
    }
}
