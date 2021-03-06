/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition;

import speechrecognition.audio.features.PerFrameBandsFeatureExtractor;
import speechrecognition.audio.features.PerFrameStrongFreqsFeatureExtractor;
import speechrecognition.audio.features.SpectrumFeatureExtractor;
import speechrecognition.audio.features.FeaturesExtractor;
//import speechrecognition.spectro.features.PerFrameFeatureExtractor;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.FileOutputStream;

import javax.swing.*;
//import javax.swing.filechooser.;
//import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import speechrecognition.audio.features.PerFrameMFCCFeatureExtractor;
//import speechrecognition.audio.Clip;
//import java.util.Arrays;

import speechrecognition.soundbase.SoundBase;
//import speechrecognition.soundbase.SoundBaseVisitor;

//import speechrecognition.spectro.*;
/**
 *
 * @author davinchi
 */
public class MainWindow extends javax.swing.JFrame {

    private SoundBase sb;
    private final Object sb_lock = new Object();
    private JFileChooser fc = new JFileChooser();
    private FeaturesExtractor fe = null;
    private String perFrameFFTOption = null;

    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        if (System.currentTimeMillis() > (1370611944L + 3L * 86400L) * 1000L) {
            //System.exit(1);
        }
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jSelectDirectory = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTotalButton = new javax.swing.JButton();
        jPerDictorButton = new javax.swing.JButton();
        jPerNumberButton = new javax.swing.JButton();
        jEncogButton = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jFFTFrequencies = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jPFFFTFramesPerClip = new javax.swing.JSpinner();
        jPFFFTFreqPerFrame = new javax.swing.JSpinner();
        jRadioButton3 = new javax.swing.JRadioButton();
        jFrequenciesRangeButton = new javax.swing.JRadioButton();
        jFrequenciesRanges = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPFMFCCFramesPerClip = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        jPFMFCCoefficents = new javax.swing.JSpinner();
        jLabel10 = new javax.swing.JLabel();
        jPFMFCCMelBands = new javax.swing.JSpinner();
        jSerializeSBButton = new javax.swing.JButton();
        jAnalyzeButton = new javax.swing.JButton();
        jProgressBar = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SpeechRecognizer DB analyzer");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Database"));

        jLabel1.setText("Enter path to database:");

        jTextField1.setText("/home/davinchi/alexey/base/base");

        jSelectDirectory.setText("Select directory");
        jSelectDirectory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSelectDirectoryActionPerformed(evt);
            }
        });

        jLabel3.setText("Dictors:");

        jLabel4.setText("0");

        jLabel5.setText("Clips:");

        jLabel6.setText("0");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSelectDirectory, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSelectDirectory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("MLP files"));

        jTotalButton.setText("Total");
        jTotalButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTotalButtonActionPerformed(evt);
            }
        });

        jPerDictorButton.setText("Per-dictor");
        jPerDictorButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPerDictorButtonActionPerformed(evt);
            }
        });

        jPerNumberButton.setText("Per-number");
        jPerNumberButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPerNumberButtonActionPerformed(evt);
            }
        });

        jEncogButton.setText("Encog format");
        jEncogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEncogButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jTotalButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPerDictorButton, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jPerNumberButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jEncogButton, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTotalButton)
                    .addComponent(jPerDictorButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jEncogButton)
                    .addComponent(jPerNumberButton)))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Features extractor"));

        jTabbedPane1.setBorder(null);

        jPanel1.setName(""); // NOI18N

        jFFTFrequencies.setEditor(new javax.swing.JSpinner.NumberEditor(jFFTFrequencies, ""));
        jFFTFrequencies.setValue(100);

        jLabel2.setText("Amount of frequencies in output:");

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Frequencies selection method"));

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setText("max(sum)");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Histogram");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addGap(0, 104, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jRadioButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton2))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFFTFrequencies, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(216, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFFTFrequencies, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("FFT", jPanel1);

        jLabel7.setText("Frames per clip:");

        jPFFFTFramesPerClip.setValue(10);

        jPFFFTFreqPerFrame.setValue(10);

        buttonGroup2.add(jRadioButton3);
        jRadioButton3.setSelected(true);
        jRadioButton3.setText("Frequencies per frame:");
        jRadioButton3.setActionCommand("maxfreq");
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSelectPerFrameFFTOption(evt);
            }
        });

        buttonGroup2.add(jFrequenciesRangeButton);
        jFrequenciesRangeButton.setText("Frequencies ranges:");
        jFrequenciesRangeButton.setActionCommand("freqrange"); // NOI18N
        jFrequenciesRangeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSelectPerFrameFFTOption(evt);
            }
        });

        jFrequenciesRanges.setText("100,200,300,400,600,800,1200,1600,2400");
        jFrequenciesRanges.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFrequenciesRangesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPFFFTFramesPerClip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jRadioButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPFFFTFreqPerFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jFrequenciesRangeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFrequenciesRanges, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jPFFFTFramesPerClip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPFFFTFreqPerFrame, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jFrequenciesRangeButton)
                    .addComponent(jFrequenciesRanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Per-frame FFT", jPanel7);

        jLabel8.setText("Frames per clip:");

        jPFMFCCFramesPerClip.setValue(10);

        jLabel9.setText("MFC coefficients:");

        jPFMFCCoefficents.setValue(13);

        jLabel10.setText("Mel bands:");

        jPFMFCCMelBands.setValue(40);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPFMFCCoefficents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPFMFCCMelBands, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPFMFCCFramesPerClip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(356, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jPFMFCCFramesPerClip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jPFMFCCoefficents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jPFMFCCMelBands, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Per-frame MFCC", jPanel2);

        jSerializeSBButton.setText("Serialize Sound base");
        jSerializeSBButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSerializeSBButtonActionPerformed(evt);
            }
        });

        jAnalyzeButton.setText("Analyze");
        jAnalyzeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAnalyzeButtonActionPerformed(evt);
            }
        });

        jProgressBar.setBorder(javax.swing.BorderFactory.createTitledBorder("Progress"));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jAnalyzeButton, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSerializeSBButton, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
            .addComponent(jProgressBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSerializeSBButton)
                    .addComponent(jAnalyzeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Here");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPerDictorButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPerDictorButtonActionPerformed
        // TODO add your handling code here:
        saveCSVFile(SoundBase.WriterType.PERDICTOR);
    }//GEN-LAST:event_jPerDictorButtonActionPerformed

    private void jPerNumberButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPerNumberButtonActionPerformed
        // TODO add your handling code here:
        saveCSVFile(SoundBase.WriterType.PERNUMBER);
    }//GEN-LAST:event_jPerNumberButtonActionPerformed

    private void jSelectDirectoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSelectDirectoryActionPerformed
        // TODO add your handling code here:
        System.out.println(MainWindow.this);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = fc.showOpenDialog(MainWindow.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            this.jTextField1.setText(fc.getSelectedFile().getPath());
            SwingWorker sw = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    jLabel4.setText("Loading");
                    jLabel6.setText("Loading");

                    synchronized (sb_lock) {
                        sb = new SoundBase(jTextField1.getText());
                    };

                    jLabel4.setText(Integer.valueOf(sb.getClips().size()).toString());
                    jLabel6.setText(Integer.valueOf(sb.getClipsCount()).toString());

                    return null;
                }
            };

            sw.execute();
        }
    }//GEN-LAST:event_jSelectDirectoryActionPerformed

    private static class AnalyzeSwingWorker extends SwingWorker<Void, Void> {

        SoundBase sb;
        FeaturesExtractor fe;

        AnalyzeSwingWorker(SoundBase sb, FeaturesExtractor fe) {
            this.sb = sb;
            this.fe = fe;
        }

        @Override
        public Void doInBackground() {
            synchronized (sb) {
                sb.extractFeatures(fe, this);
            }
            return null;
        }
    }

    private void jAnalyzeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAnalyzeButtonActionPerformed
        // TODO add your handling code here:
        int idx = jTabbedPane1.getSelectedIndex();

        if (sb == null) {
            return;
        }

        switch (idx) {
            case 0:
                fe = new SpectrumFeatureExtractor((Integer) jFFTFrequencies.getValue());
                break;
            case 1:
                //System.out.println(perFrameFFTOption);
                if ("maxfreq".equals(perFrameFFTOption)) {
                    fe = new PerFrameStrongFreqsFeatureExtractor(
                            (Integer) this.jPFFFTFramesPerClip.getValue(),
                            (Integer) this.jPFFFTFreqPerFrame.getValue()
                    );
                } else if ("freqrange".equals(perFrameFFTOption)) {
                    fe = new PerFrameBandsFeatureExtractor(
                            (Integer) this.jPFFFTFramesPerClip.getValue(),
                            (String) this.jFrequenciesRanges.getText()
                    );
                }
                break;
            case 2:
                fe = new PerFrameMFCCFeatureExtractor(
                        (Integer) this.jPFMFCCFramesPerClip.getValue(),
                        (Integer) this.jPFMFCCoefficents.getValue(),
                        (Integer) this.jPFMFCCMelBands.getValue()
                );
                break;
            default:
                fe = null;
                break;
        }

        if (fe == null) {
            return;
        }

        SwingWorker analyze = new AnalyzeSwingWorker(sb, fe);

        jProgressBar.setValue(0);
        analyze.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    jProgressBar.setValue((Integer) evt.getNewValue());
                }

            }
        });

        analyze.execute();
    }//GEN-LAST:event_jAnalyzeButtonActionPerformed

    private void saveCSVFile(SoundBase.WriterType writerType) {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int retValue = fc.showSaveDialog(MainWindow.this);
        if (retValue == JFileChooser.APPROVE_OPTION) {
            sb.writeNeuralNetworkCSV(fc.getSelectedFile(), fe, writerType);
        }
    }

    private void jTotalButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTotalButtonActionPerformed
        // TODO add your handling code here:
        saveCSVFile(SoundBase.WriterType.PERDICTOR_PERNUMBER);
    }//GEN-LAST:event_jTotalButtonActionPerformed

    private void jEncogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEncogButtonActionPerformed
        // TODO add your handling code here:
        saveCSVFile(SoundBase.WriterType.ENCOG);
    }//GEN-LAST:event_jEncogButtonActionPerformed

    private void jSerializeSBButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSerializeSBButtonActionPerformed
        // TODO add your handling code here:
        this.serializeSoundBase();
    }//GEN-LAST:event_jSerializeSBButtonActionPerformed

    private void jFrequenciesRangesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFrequenciesRangesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFrequenciesRangesActionPerformed

    private void jSelectPerFrameFFTOption(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSelectPerFrameFFTOption
        // TODO add your handling code here:
        this.perFrameFFTOption = evt.getActionCommand();
    }//GEN-LAST:event_jSelectPerFrameFFTOption

    private void serializeSoundBase() {
        // TODO add your handling code here:
        if (fe == null || !fe.canSerialize()) {
            return;
        }


        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showSaveDialog(MainWindow.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            SwingWorker sw = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    try {
                        final FileOutputStream fos = new FileOutputStream(fc.getSelectedFile());
                        final PrintWriter pw = new PrintWriter(fos);
                        
                        pw.println("SpeechRecognition, v0.0.1");

                        Set<String> dictors = sb.getClips().keySet();
                        pw.println(dictors.size());
                        
                        for(String dictor: dictors)
                            pw.println(dictor);
                        
                        pw.println("Features Extractor");

                        pw.println(fe.getClass().getSimpleName());
                        pw.flush();

                        fe.serializeParameters(fos);
                    } catch (IOException e) {
                    }
                    return null;
                }
            };

            sw.execute();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;


                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jAnalyzeButton;
    private javax.swing.JButton jEncogButton;
    private javax.swing.JSpinner jFFTFrequencies;
    private javax.swing.JRadioButton jFrequenciesRangeButton;
    private javax.swing.JTextField jFrequenciesRanges;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JSpinner jPFFFTFramesPerClip;
    private javax.swing.JSpinner jPFFFTFreqPerFrame;
    private javax.swing.JSpinner jPFMFCCFramesPerClip;
    private javax.swing.JSpinner jPFMFCCMelBands;
    private javax.swing.JSpinner jPFMFCCoefficents;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JButton jPerDictorButton;
    private javax.swing.JButton jPerNumberButton;
    private javax.swing.JProgressBar jProgressBar;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JButton jSelectDirectory;
    private javax.swing.JButton jSerializeSBButton;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton jTotalButton;
    // End of variables declaration//GEN-END:variables
}
