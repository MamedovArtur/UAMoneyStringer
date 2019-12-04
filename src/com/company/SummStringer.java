package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SummStringer {

    private long uah;
    private long coins;


    private static class WordForms {
        private String nameOneForm;//именительный, единственное
        private String bornOneForm;//родительный, единственное
        private String bornManyForm;//родительный, множественное
        private int sex;

        public WordForms(String nameOneForm, String bornOneForm, String bornManyForm, int sex) {
            this.nameOneForm = nameOneForm;
            this.bornOneForm = bornOneForm;
            this.bornManyForm = bornManyForm;
            this.sex = sex;
        }

        public String getNameOneForm() {
            return nameOneForm;
        }

        public String getBornOneForm() {
            return bornOneForm;
        }

        public String getBornManyForm() {
            return bornManyForm;
        }

        public int getSex() {
            return sex;
        }


        public String getFormByCount(long count) {
            long n = count % 100;
            long n1 = n % 10;
            if (n > 10 && n < 20) return bornManyForm;
            if (n1 > 1 && n1 < 5) return bornOneForm;
            if (n1 == 1) return nameOneForm;
            return bornManyForm;
        }
    }


    private final static String[][] str1 = {
            {"", "один", "два", "три", "чотири", "п'ять", "шість", "сім", "вісім", "дев'ять"},
            {"", "одна", "дві", "три", "чотири", "п'ять", "шість", "сім", "вісім", "дев'ять"},
    };
    private final static String[] str100 = {"", "сто", "двісті", "триста", "чотириста", "п'ятсот", "шістсот", "сімсот", "вісімсот", "дев'ятсот"};
    private final static String[] str11 = {"десять", "одиннадцать", "двінадцять", "тринадцять", "чотирнадцять", "п'ятнадцять", "шістнадцять", "сімнадцять", "вісімнадцять", "дев'ятнадцять", "двадцять"};
    private final static String[] str10 = {"", "десять", "двадцять", "тридцять", "сорок", "п'ятдесят", "шістдесят", "сімдесят", "вісімдесят", "дев'яносто"};

    private final static WordForms[] forms = {
            new WordForms("копійка", "копійки", "копійок", 1),
            new WordForms("гривня", "гривні", "гривень", 1),
            new WordForms("тисяча", "тисячі", "тисяч", 1),
            new WordForms("мільйон", "мільйона", "мільйонів", 0),
            new WordForms("мільярд", "мільярда", "мільярдів", 0),
            new WordForms("трильйон", "трильйонів", "трильярдів", 0)
    };


    public SummStringer(long uah, long coins) {
        this.uah = uah;
        this.coins = coins;
    }
    public SummStringer(double summ) {
        long summInCoin = (long)(summ*100);
        this.uah = summInCoin/100;
        this.coins = summInCoin%100;
    }


    /***
     * Разбивает число на сегменты по 3 цифры
     * @param num - число
     * @return List<Integer> - список сегментов
     */
    private List<Integer> splitSegments(long num) {
        List<Integer> segments = new ArrayList<>();
        while (num > 999) {
            segments.add((int) (num % 1000));
            num = num / 1000;
        }
        segments.add((int) num);
        Collections.reverse(segments);
        return segments;
    }


    /**
     * Преобразует трехзначное число (сегмент) в строковую форму с учетом рода (один/одна)
     * @param segment - трехзначное число
     * @param sex - род (0-м, 1-ж)
     * @return String
     */
    private String segmentToString(int segment, int sex) {
        int i100 = segment / 100;
        String s = i100>0? str100[i100] + " ":"";
        int i11 = segment % 100;
        if (i11 > 9 && i11 <= 20) {
            s += str11[i11 - 10] + " ";
        } else {
            int i10 = i11 / 10;
            int i1 = i11 % 10;

            if (i10 > 0) s += str10[i10] + " ";
            if (i1 > 0) s += str1[sex][i1] + " ";
        }
        return s;
    }


    private String uahToStr() {
        if (uah == 0) return "нуль гривень ";
        List<Integer> segments = splitSegments(uah);

        StringBuilder outStr = new StringBuilder();
        int formsIndex = segments.size();
        for (Integer segment : segments) {
            WordForms form = forms[formsIndex--];
            outStr.append(segmentToString(segment, form.getSex()));
            outStr.append(form.getFormByCount(segment));
            outStr.append(" ");
        }

        return outStr.toString();
    }

    private String coinsToStr() {
        if (coins == 0) return "нуль копійок";
        WordForms form = forms[0];
        return segmentToString((int) coins, form.getSex())
                + form.getFormByCount((int) coins);

    }


    @Override
    public String toString() {
        return uahToStr() + coinsToStr();
    }

}
