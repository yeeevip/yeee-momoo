package vip.yeee.memoo.demo.tokenizing.jcseg.example;

import cn.hutool.core.collection.IterUtil;
import cn.hutool.extra.tokenizer.Word;
import vip.yeee.memoo.common.tokenizing.jcseg.customize.ADictionaryExtra;

import java.util.Iterator;

/**
 * description......
 *
 * @author https://www.yeee.vip
 * @since 2023/4/25 16:32
 */
public class JcsegExample {

    public static void main(String[] args) {
        Iterator<Word> it;String seg;
        String cc = "这 是\n一段演示语句。";
        it = ADictionaryExtra.getTokenizerEngine().parse(cc);
        if(IterUtil.isNotEmpty(it)) {
            while(it.hasNext()) {
                seg = it.next().getText();
                System.out.println(seg);
            }
        }
    }

}
