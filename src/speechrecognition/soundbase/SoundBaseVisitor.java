/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package speechrecognition.soundbase;

import speechrecognition.audio.Clip;

/**
 *
 * @author davinchi
 */
public interface SoundBaseVisitor {
    public void visitDictor(String name);
    public void visitNumber(Integer number);
    public void visitClip(Clip element);
}
