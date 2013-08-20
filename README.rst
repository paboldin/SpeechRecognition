.. _CMU-Sphinx: http://cmusphinx.sourceforge.net/sphinx4/
.. _Neural Network: http://en.wikipedia.org/wiki/Artificial_neural_network
.. _Encog: http://code.google.com/p/encog-java/
.. _SpectroEdit: http://code.google.com/p/spectro-edit/
.. _funf-open-sensing-framework: http://code.google.com/p/funf-open-sensing-framework/
.. _MFCC: http://en.wikipedia.org/wiki/Mel-frequency_cepstrum

=========================
SpeechRecognition project
=========================

Introduction
------------

NOTE: Yes, there is CMU-Sphinx_.

This project provides basic "speech" recognition system (digits only)
starting from preparing studying sample for `Neural Network`_ from given
database of sound to software that records audio from system input
and tries to categorize it using trained Neural Network.

Project was written as diploma software for my customer.

Neural Network library used is Encog_.

Code contains parts of SpectroEdit_ project and funf-open-sensing-framework_.

Workflow
--------

Building training set
`````````````````````

First user must prepare database of samples of sounds with following
directory structure:

    <Dictor>/<Number>/<Samplefile#1>.wav
    <Dictor>/<Number>/<Samplefile#2>.wav

It is important not to mess these files up, as this will degrade
recognition performance greatly.

Main application then uses it to build a database of features,
using selected FeatureExtractor. This produces two output files:
one with FeatureExtractor parameters and other with .csv file
suitable for use in Encog_ to train a Multi-Layer Perceptron.

Most sucessfull FeatureExtractor is MFCC_ --
Mel-Frequency Cepstrum Coefficients.

Using neural network
````````````````````

Second step is to run recognizer (either from NetBeans or with appropriate
command). Recognizer requires FeatureExtractor paremeters' file as 
well as file with trained neuron network. GUI is semi-obviously.
