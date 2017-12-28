package com.tonylinzhen.test;

import com.sun.deploy.util.ArrayUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CsvRow2Col {
    public static void main(String[] args) {

        String filePath = "标签导出.csv";
        List<String[]> objects = readCsv(filePath);

        int row2col = 1;

        HashMap<String, Integer> header = getHeaderIndex(objects, row2col);

        export("result.csv", objects, header, row2col);

    }

    private static void export(String s, List<String[]> objects, HashMap<String, Integer> header, int row2col) {
        try {
            FileWriter fileWriter = new FileWriter(s);

            HashMap<String, String[]> result = new HashMap<String, String[]>();
            String[] strings1 = objects.get(0);
            int length = strings1.length - 1 + header.size();

            StringBuilder h = new StringBuilder();
            for (int i = 0; i < length; i++) {

                if (i == 0) {
                    h.append("c" + i).append(",");

                } else {
                    if (i >= row2col && i < row2col + length - 2) {
                        Set<Map.Entry<String, Integer>> entries = header.entrySet();
                        for (Map.Entry<String, Integer> entry : entries) {
                            if (entry.getValue() == i - row2col) {
                                h.append(entry.getKey()).append(",");
                            }
                        }
                    } else {
                        h.append("c" + i).append(",");
                    }
                }
            }
            h.setCharAt(h.length() - 1, '\n');
            fileWriter.write(h.toString());

            for (String[] object : objects) {

                for (int i = 0; i < length; i++) {

                    if (i == 0) {
                        String[] strings = result.get(object[0]);
                        if (strings == null) {
                            String[] objs = new String[length];
                            result.put(object[0], objs);
                            objs[i] = object[0];


                        }
                    } else {
                        String[] objs = result.get(object[0]);
                        if (i >= row2col && i < row2col + length - 2) {
                            objs[header.get(object[row2col])] = "1";
                        } else {
                            objs[i] = object[i - length + 3];
                        }
                    }
                }
            }


            for (Map.Entry<String, String[]> entry : result.entrySet()) {
                String[] value = entry.getValue();
                StringBuilder stringBuilder = new StringBuilder();
                for (String s1 : value) {
                    stringBuilder.append(s1).append(",");
                }
                stringBuilder.setCharAt(stringBuilder.length() - 1, '\n');
                fileWriter.write(stringBuilder.toString());
            }

            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HashMap<String, Integer> getHeaderIndex(List<String[]> objects, int row2col) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();

        for (Object[] object : objects) {
            Integer integer = map.get(object[row2col]);
            if (integer == null) {
                map.put((String) object[row2col], map.size());
            }
        }

        return map;
    }

    private static List<String[]> readCsv(String s) {
        List<String[]> list = new ArrayList<String[]>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(new File(s));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter("[\r\n]+");
        for (; scanner.hasNext(); ) {
            String next = scanner.next();
            String[] obj = next.split(",");
            list.add(obj);
        }

        scanner.close();
        return list;
    }

}
