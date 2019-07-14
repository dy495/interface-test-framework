package com.haisheng.framework.testng.CommonDataStructure;

import org.testng.annotations.DataProvider;

public class CommodityManaAccuracyRate {

    private static String outOfStock = "OUT_OF_STOCK";
    private static String wrongPlace = "WRONG_PLACE";
    private static String wrongPlaceAndOutOfStock = "WRONG_PLACE:OUT_OF_STOCK";


    @DataProvider(name = "ACCURACY_RATE")
    private static Object[][] testAccuracyRate() {
        //Dchng  Dtotal bindingStock  bindingTotal DwrongChng DwrongTotal, wrongAlarm  wrongStock
        //Pchng1 Ptotal1  {alarm1} stock1    Pchng2 Ptotal2  {alarm2} stock2
        //Pchng3 Ptotal3  {alarm3} stock3   Pchng4 Ptotal4  {alarm4} stock4
        //Pchng5 Ptotal5  {alarm5} stock5    Pchng6 Ptotal6  {alarm6} stock6
        Object[][] objects = {
                // 5对1错   六个商品单独拿
//                --------------------------------------1-6---------------------------------------------------------
                new Object[]{
                        1,
                         100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -100, 350, wrongPlace,              3,
                        -100, 250, wrongPlaceAndOutOfStock,      2, -100, 150, wrongPlaceAndOutOfStock, 1,
                        -100, 50,  wrongPlaceAndOutOfStock,      0, -50,  -3,  outOfStock,              0
                },
                new Object[]{
                        2,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -100, 350, wrongPlace,              3,
                        -100, 250, wrongPlaceAndOutOfStock,      2, -100, 150, wrongPlaceAndOutOfStock, 1,
                        -50, 100,  outOfStock,                   1, -100, -3,  outOfStock,              0
                },
                new Object[]{
                        3,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -100, 350, wrongPlace,              3,
                        -100, 250, wrongPlaceAndOutOfStock,      2, -50, 200,  outOfStock,              2,
                        -100, 100, outOfStock,                   1, -100, -3,  outOfStock,              0
                },
                new Object[]{
                        4,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -100, 350, wrongPlace,              3,
                        -50, 300,  "",                           3, -100, 200, outOfStock,              2,
                        -100, 100, outOfStock,                   1, -100, -3,  outOfStock,              0
                },
                new Object[]{
                        5,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -50, 400,  "",                      4,
                        -100, 300, "",                           3, -100, 200, outOfStock,              2,
                        -100, 100, outOfStock,                   1, -100, -3,  outOfStock,              0
                },

                new Object[]{
                        6,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50, 500,  "",                           5, -100, 400, "",                      4,
                        -100, 300, "",                           3, -100, 200, outOfStock,              2,
                        -100, 100, outOfStock,                   1, -100, -3,  outOfStock,              0
                },

                // 5对1错   一起拿一对一错，其他四个正确的单独拿
//                ----------------------------------------------7-11--------------------------------------------

                new Object[]{
                        7,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -150, 400, "",                           4, -100, 300,  "",                     3,
                        -100, 200, outOfStock,                   2, -100, 100, outOfStock,              1,
                        -100, -3,  outOfStock,                   0,  0,   -3,  outOfStock,              0,
                },
                new Object[]{
                        8,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -150, 300,  "",                     3,
                        -100, 200, outOfStock,                   2, -100, 100, outOfStock,              1,
                        -100, -3,  outOfStock,                   0,  0,   -3,  outOfStock,              0,
                },
                new Object[]{
                        9,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -100, 350,  wrongPlace,             3,
                        -150, 200, outOfStock,                   2, -100, 100, outOfStock,              1,
                        -100, -3,  outOfStock,                   0,  0,   -3,  outOfStock,              0,
                },
                new Object[]{
                        10,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -100, 350,  wrongPlace,             3,
                        -100, 250, wrongPlaceAndOutOfStock,      2, -150, 100,  outOfStock,             1,
                        -100, -3,  outOfStock,                   0,  0,   -3,  outOfStock,              0,
                },
                new Object[]{
                        11,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100, 450, wrongPlace,                   4, -100, 350,  wrongPlace,             3,
                        -100, 250, wrongPlaceAndOutOfStock,      2, -100, 150,  wrongPlaceAndOutOfStock,1,
                        -150, -3,  outOfStock,                   0,  0,   -3,  outOfStock,              0,
                },

                // 5对1错   一起拿两个对的，其他3个正确的和一个错的单独拿
//                ---------------------------------------------12-15-------------------------------
                new Object[]{
                        12,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -200, 350, wrongPlace,                   3, -100, 250,  wrongPlaceAndOutOfStock,2,
                        -100, 150, wrongPlaceAndOutOfStock,      1, -100, 50,   wrongPlaceAndOutOfStock,0,
                        -50,  -3,  outOfStock,                   0,  0,   -3,  outOfStock,              0,
                },
                new Object[]{
                        13,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -200, 350, wrongPlace,                   3, -100, 250,  wrongPlaceAndOutOfStock,2,
                        -100, 150, wrongPlaceAndOutOfStock,      1, -50, 100,   outOfStock,             1,
                        -100,  -3,  outOfStock,                  0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        14,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -200, 350, wrongPlace,                   3, -100, 250,  wrongPlaceAndOutOfStock,2,
                        -50,  200, outOfStock,                   2, -100, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        15,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -200, 350, wrongPlace,                   3, -50, 300,   "",                     3,
                        -100,  200, outOfStock,                   2, -100, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,             0,
                },

//                --------------------------------------16-20-----------------------------------------------------------------------------------------------
                new Object[]{
                        16,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,   "",                        5, -200, 300,   "",                     3,
                        -100,  200, outOfStock,                   2, -100, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        17,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,   "",                        5, -100, 400,   "",                     4,
                        -200,  200, outOfStock,                  2, -100, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        18,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,   "",                        5, -100, 400,   "",                     4,
                        -100,  300,   "",                        3, -200, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        19,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,   "",                        5, -100, 400,   "",                     4,
                        -100,  300,   "",                        3, -100, 200,  outOfStock,             2,
                        -200,  -3, outOfStock,                   0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        20,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -50, 400,   "",                     4,
                        -100,   300,   "",                        3, -100, 200,  outOfStock,             2,
                        -200,  -3, outOfStock,                   0,  0,   -3,   outOfStock,             0,
                },

//                --------------------------------------21-25-----------------------------------------------------------------------------------------------
                new Object[]{
                        21,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -50, 400,   "",                     4,
                        -100,   300,   "",                        3, -200, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        22,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -50, 400,   "",                     4,
                        -200,   200,   outOfStock,                2, -100, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        23,
                         100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -100, 350,   wrongPlace,             3,
                        -50,    300,   "",                        3, -100, 200,  outOfStock,             2,
                        -200,  -3, outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        24,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -100, 350,   wrongPlace,             3,
                        -50,    300,   "",                        3, -200, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        25,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -100, 350,   wrongPlace,             3,
                        -100,   250,   wrongPlaceAndOutOfStock,   2, -50, 200,  outOfStock,               2,
                        -200,  -3,     outOfStock,                0,  0,   -3,   outOfStock,             0,
                },

                //----------------26-30-------------------------------------------------------------------------------------------------------------
                new Object[]{
                        26,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -100, 350,   wrongPlace,             3,
                        -100,   250,   wrongPlaceAndOutOfStock,   2, -200, 50,    wrongPlaceAndOutOfStock,0,
                        -50,  -3,      outOfStock,                0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        27,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -100, 350,   wrongPlace,             3,
                        -200,   150,   wrongPlaceAndOutOfStock,   1, -100, 50,    wrongPlaceAndOutOfStock,0,
                        -50,  -3,      outOfStock,                0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        28,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -100, 350,   wrongPlace,             3,
                        -200,   150,   wrongPlaceAndOutOfStock,   1, -50, 100,    outOfStock,             1,
                        -100,  -3,      outOfStock,                0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        29,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -200, 250,   wrongPlaceAndOutOfStock,2,
                        -100,   150,   wrongPlaceAndOutOfStock,   1, -100, 50,    wrongPlaceAndOutOfStock,0,
                        -50,  -3,      outOfStock,                0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        30,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -200, 250,   wrongPlaceAndOutOfStock,2,
                        -100,   150,   wrongPlaceAndOutOfStock,   1, -50, 100,    outOfStock,             1,
                        -100,  -3,      outOfStock,               0,  0,   -3,   outOfStock,             0,
                },

                //----------------31-35-------------------------------------------------------------------------------------------------------------
                new Object[]{
                        31,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -200, 250,   wrongPlaceAndOutOfStock,2,
                        -50,    200,   outOfStock,                2, -100, 100,    outOfStock,            1,
                        -100,  -3,      outOfStock,               0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        32,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -300,   250,   wrongPlaceAndOutOfStock,   2, -100, 150,   wrongPlaceAndOutOfStock,1,
                        -100,   50,    wrongPlaceAndOutOfStock,    0, -50,  -3,    outOfStock,         0,
                        0,  -3,      outOfStock,               0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        33,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -300,   250,   wrongPlaceAndOutOfStock,   2, -100, 150,   wrongPlaceAndOutOfStock,1,
                        -50,   100,    outOfStock,                1, -100,  -3,    outOfStock,            0,
                         0,  -3,      outOfStock,               0,  0,   -3,   outOfStock,             0,
                },

                new Object[]{
                        34,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -300,   250,   wrongPlaceAndOutOfStock,   2, -50, 200,   outOfStock,               2,
                        -100,   100,    outOfStock,               1, -100,  -3,    outOfStock,            0,
                        0,  -3,      outOfStock,               0,  0,   -3,   outOfStock,             0,
                },

                new Object[]{
                        35,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                   4, -300, 150,   wrongPlaceAndOutOfStock,1,
                        -100,   50,    wrongPlaceAndOutOfStock,      0, -50,  -3,    outOfStock,             0,
                        0,  -3,      outOfStock,                     0,  0,   -3,   outOfStock,             0,
                },

//                ----------------36-40-------------------------------------------------------------------------------------------------------------

                new Object[]{
                        36,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -300, 150,   wrongPlaceAndOutOfStock,1,
                        -50,   100,    outOfStock,                  1, -100,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        37,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -100, 350,   wrongPlace,             3,
                        -50,   300,    "",                          3, -300,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        37,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -100, 350,   wrongPlace,             3,
                        -300,   50,    wrongPlaceAndOutOfStock,     0, -50,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        39,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -50, 400,   "",                      4,
                        -100,   300,    "",                         3, -300,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        40,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -50, 400,   "",                      4,
                        -300,   100,    outOfStock,                 1, -100,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },

                //                ----------------41-45-------------------------------------------------------------------------------------------------------------
                new Object[]{
                        41,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,   "",                           5, -300, 200,   outOfStock,             2,
                        -100,   100,    outOfStock,                 1, -100,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        42,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,   "",                           5, -100, 400,   "",                    4,
                        -300,   100,    outOfStock,                 1, -100,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        43,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,   "",                           5, -100, 400,   "",                    4,
                        -100,   300,    "",                         3, -300,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },

                new Object[]{
                        44,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -250,   300,   "",                           3, -100, 200,   outOfStock,           2,
                        -100,   100,    outOfStock,                  1, -300,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        45,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                   4, -250, 200,   outOfStock,           2,
                        -100,   100,    outOfStock,                  1, -100,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },

//                ----------------46-50-------------------------------------------------------------------------------------------------------------

                new Object[]{
                        46,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                   4, -100, 350,   wrongPlace,            3,
                        -250,   100,    outOfStock,                  1, -100,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },

                new Object[]{
                        47,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                   4, -100, 350,   wrongPlace,             3,
                        -100,   250,    wrongPlaceAndOutOfStock,     2, -250,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        48,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -400,   150,   wrongPlaceAndOutOfStock,      1, -100, 50,   wrongPlaceAndOutOfStock,0,
                        -100,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        49,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -400,   150,   wrongPlaceAndOutOfStock,      1, -50, 100,   outOfStock,           1,
                        -100,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        50,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -400, 50,   wrongPlaceAndOutOfStock,0,
                        -50,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },

//                ----------------51-55-------------------------------------------------------------------------------------------------------------
                new Object[]{
                        51,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -50, 400,   "" ,                 4,
                        -400,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        52,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,    "",                           5, -100, 400,   "" ,                 4,
                        -400,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        53,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,    "",                            5, -400, 100,   outOfStock ,           1,
                        -100,   -3,    outOfStock,                    0, 0,  -3,   outOfStock,             0,
                        0,  -3,        outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        54,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -350,   200,   outOfStock,                  2, -100, 100,   outOfStock ,           1,
                        -100,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        55,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -350, 100,   outOfStock ,           1,
                        -100,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },

//                ----------------56-60-------------------------------------------------------------------------------------------------------------
                new Object[]{
                        56,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                  4, -100, 350,   wrongPlace ,           3,
                        -350,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        57,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -500,   50,   wrongPlaceAndOutOfStock,       0, -50, -3,   outOfStock ,           0,
                        0,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        58,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -50,   500,   "",                         5, -500, -3,   outOfStock ,           0,
                        0,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        59,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -450,   100,   outOfStock,                1, -100, -3,   outOfStock ,           0,
                        0,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },
                new Object[]{
                        60,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -100,   450,   wrongPlace,                4, -450, -3,   outOfStock ,           0,
                        0,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },

//                ----------------61-------------------------------------------------------------------------------------------------------------
                new Object[]{
                        61,
                        100, 500, 5, 500,  50, 550, wrongPlace, 5,
                        -550,   -3,   outOfStock,                0, 0, -3,   outOfStock ,           0,
                        0,   -3,    outOfStock,                   0, 0,  -3,   outOfStock,             0,
                        0,  -3,      outOfStock,                    0,  0,   -3,   outOfStock,             0,
                },

//                正确商品4，错误商品1
//------------------------------------------1-5-五个商品都单独拿------------------------------------------------------------------------------------------------------------

                new Object[]{
                        62,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -50,   400,   "",                         4, -100, 300,   "",                    3,
                        -100,   200,   outOfStock,                2, -100, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        63,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,               3, -50, 300,   "",                    3,
                        -100,   200,   outOfStock,                2, -100, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        64,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,               3, -100, 250,   wrongPlaceAndOutOfStock,2,
                        -50,   200,   outOfStock,                2, -100, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        65,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,               3, -100, 250,   wrongPlaceAndOutOfStock,2,
                        -100,   150,   wrongPlaceAndOutOfStock,  1, -50, 100,  outOfStock,             1,
                        -100,  -3, outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        66,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,               3, -100, 250,   wrongPlaceAndOutOfStock,2,
                        -100,   150,   wrongPlaceAndOutOfStock,  1, -100, 50,    wrongPlaceAndOutOfStock,0,
                        -50,  -3, outOfStock,                    0,  0,   -3,   outOfStock,              0,
                },

//        ------------------------------6-9- 1错1对一起拿，其他三个对的单独拿------------------------------------------------------------------------------------------------------------
                new Object[]{
                        67,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -150,   300,   "",                           3, -100, 200,   outOfStock,           2,
                        -100,   100,   outOfStock,                   1, -100, -3,    outOfStock,           0,
                        0,  -3, outOfStock,                          0,  0,   -3,   outOfStock,              0,
                },

                new Object[]{
                        68,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -150, 200,   outOfStock,    2,
                        -100,   100,   outOfStock,                   1, -100, -3,    outOfStock,           0,
                           0,    -3,   outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },

                new Object[]{
                        69,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -100, 250,   wrongPlaceAndOutOfStock,2,
                        -150,   100,   outOfStock,                   1, -100, -3,    outOfStock,           0,
                           0,   -3,    outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        70,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -100, 250,   wrongPlaceAndOutOfStock,2,
                        -100,   150,   wrongPlaceAndOutOfStock,      1, -150, -3,    outOfStock,           0,
                        0,  -3, outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },

//   -------------------------10-21--两个对的一起拿，其他三个单独拿----------------------------------------------------------------------------------------------------------

//                ------------------------------------10-15------------------------------------------------------------

                new Object[]{
                        71,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -200,   250,   wrongPlaceAndOutOfStock,          2, -100, 150,   wrongPlaceAndOutOfStock,1,
                        -100,   50,    wrongPlaceAndOutOfStock,          0, -50,  -3,    outOfStock,           0,
                           0,   -3,    outOfStock,                       0,  0,   -3,    outOfStock,              0,
                },
                new Object[]{
                        72,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -200,   250,   wrongPlaceAndOutOfStock,           2, -100, 150,   wrongPlaceAndOutOfStock,1,
                        -50,   100,    outOfStock,                        1, -100, -3,    outOfStock,           0,
                          0,    -3,    outOfStock,                        0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        73,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -200,   250,   wrongPlaceAndOutOfStock,          2, -50, 200,   outOfStock,             2,
                        -100,   100,   outOfStock,                        1, -100, -3,    outOfStock,           0,
                        0,  -3, outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        74,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -200, 150,   wrongPlaceAndOutOfStock,   1,
                        -100,   50,   wrongPlaceAndOutOfStock,       0, -50, -3,     outOfStock,           0,
                        0,  -3, outOfStock,                   0,  0,   -3,   outOfStock,              0,
                },
                new Object[]{
                        75,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -200, 150,   wrongPlaceAndOutOfStock,   1,
                        -50,    100,   outOfStock,                   1, -100, -3,    outOfStock,                0,
                          0,     -3,   outOfStock,                   0,  0,   -3,   outOfStock,                 0,
                },
                new Object[]{
                        76,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -100, 250,   wrongPlaceAndOutOfStock, 2,
                        -200,    50,   wrongPlaceAndOutOfStock,      0, -50, -3,    outOfStock,               0,
                           0,    -3,   outOfStock,                   0,  0,   -3,   outOfStock,               0,
                },

//     ----------------------------------16-21------------------------------------------------------------------------------------
                new Object[]{
                        77,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -100, 250,   wrongPlaceAndOutOfStock,  2,
                         -50,   200,   outOfStock,                   2, -200,  -3,   outOfStock,               0,
                           0,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,               0,
                },
                new Object[]{
                        78,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -50, 300,   "",             3,
                        -200,   100,   outOfStock,                   1, -100, -3,    outOfStock,    0,
                           0,    -3,   outOfStock,                   0,  0,   -3,   outOfStock,     0,
                },
                new Object[]{
                        79,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -50, 300,   "",             3,
                        -100,   200,   outOfStock,                   2, -200, -3,    outOfStock,    0,
                           0,    -3,   outOfStock,                   0,  0,   -3,    outOfStock,    0,
                },
                new Object[]{
                        80,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -50,   400,     "",                          4, -100, 300,   "",             3,
                        -100,   200,   outOfStock,                   2, -200, -3,    outOfStock,     0,
                           0,    -3,   outOfStock,                   0,  0,   -3,    outOfStock,     0,
                },

                new Object[]{
                        81,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -50,   400,    "",                           4, -100, 300,   "",             3,
                        -200,  100,   outOfStock,                    1, -100, -3,    outOfStock,     0,
                           0,  -3,    outOfStock,                    0,  0,   -3,    outOfStock,     0,
                },
                new Object[]{
                        82,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -50,   400,   "",                            4, -200, 200,   outOfStock,     2,
                        -100,   100,   outOfStock,                   1, -100, -3,    outOfStock,     0,
                             0,  -3,   outOfStock,                   0,  0,   -3,    outOfStock,     0,
                },

//                --------------------22-27- 三个对的一起拿，其他两个单独拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        83,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -300,   150,   wrongPlaceAndOutOfStock,      1, -100, 50,   wrongPlaceAndOutOfStock,0,
                         -50,     0,   outOfStock,                   0,    0, -3,    outOfStock,            0,
                           0,    -3,   outOfStock,                   0,    0, -3,    outOfStock,            0,
                },
                new Object[]{
                        84,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -300,   150,   wrongPlaceAndOutOfStock,      1, -50, 100,    outOfStock,           1,
                        -100,   -3,    outOfStock,                   0,   0,  -3,    outOfStock,           0,
                           0,   -3,    outOfStock,                   0,   0,  -3,    outOfStock,           0,
                },
                new Object[]{
                        85,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -300, 50,   wrongPlaceAndOutOfStock,0,
                         -50,    -3,   outOfStock,                   0,    0, -3,   outOfStock,             0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,             0,
                },
                new Object[]{
                        86,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -50,   400,   "",                            4, -300, 100,   outOfStock,            1,
                        -100,   -3,   outOfStock,                    0,    0,  -3,   outOfStock,            0,
                           0,   -3,   outOfStock,                    0,    0,  -3,   outOfStock,            0,
                },
                new Object[]{
                        87,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -50, 300,   "",                     3,
                        -300,    -3,   outOfStock,                   0,   0,  -3,    outOfStock,            0,
                           0,    -3,   outOfStock,                   0,   0,  -3,    outOfStock,            0,
                },
                new Object[]{
                        88,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -50,   400,   "",                            4, -100, 300,   "",                    3,
                        -300,   -3,   outOfStock,                    0, 0, -3,       outOfStock,            0,
                          0,    -3,   outOfStock,                    0,  0,   -3,    outOfStock,            0,
                },

//       -----------------------------------------------28-30- 三个对的一起拿，其他两个单独拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        89,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -100, 250,   wrongPlaceAndOutOfStock, 2,
                        -250,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,              0,
                           0,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,              0,
                },
                new Object[]{
                        90,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -250, 100,   outOfStock,         1,
                        -100,    -3,   outOfStock,                   0,    0, -3,    outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,    outOfStock,         0,
                },
                new Object[]{
                        91,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -250,   200,   outOfStock,                   2, -100, 100,   outOfStock,         1,
                        -100,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,         0,
                },
//       -----------------------------------------------31-32- 单独拿一个对的，其他一起拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        92,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -100,   350,   wrongPlace,                   3, -350, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,         0,
                },
                new Object[]{
                        93,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -350,   100,   outOfStock,                   1, -100, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,         0,
                },

//       -----------------------------------------------33-34- 单独拿一个对的，其他一起拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        94,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -400,   50,   wrongPlaceAndOutOfStock,       0, -50, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0,   0, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0,   0, -3,   outOfStock,         0,
                },
                new Object[]{
                        95,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -50,   400,   "",                            4, -400, -3,   outOfStock,         0,
                          0,    -3,   outOfStock,                    0,    0, -3,   outOfStock,         0,
                          0,    -3,   outOfStock,                    0,    0, -3,   outOfStock,         0,
                },

//    -------------------------------------35-五个全一起拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        96,
                        100, 400, 4, 400,  50, 450, wrongPlace, 4,
                        -450,   -3,   outOfStock,                    0, 0, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0, 0, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0, 0, -3,   outOfStock,         0,
                },

//                3个正确的和1个错误的

//   -----------------------------------------1-4-4个全单独拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        97,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -50,    300,           "",                   3, -100, 200,   outOfStock,         2,
                        -100,   100,   outOfStock,                   1, -100,  -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,         0,
                },
                new Object[]{
                        98,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -100,   250,   wrongPlaceAndOutOfStock,      2, -50, 200,    outOfStock,         2,
                        -100,   100,   outOfStock,                   1, -100, -3,    outOfStock,         0,
                           0,    -3,   outOfStock,                   0,  0,   -3,    outOfStock,         0,
                },
                new Object[]{
                        99,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -100,   250,   wrongPlaceAndOutOfStock,      2, -100, 150,   wrongPlaceAndOutOfStock, 1,
                         -50,   100,   outOfStock,                   1, -100, -3,    outOfStock,              0,
                           0,    -3,   outOfStock,                   0,  0,   -3,    outOfStock,              0,
                },
                new Object[]{
                        100,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -100,   250,   wrongPlaceAndOutOfStock,      2, -100, 150,   wrongPlaceAndOutOfStock, 1,
                        -100,    50,   wrongPlaceAndOutOfStock,      0,  -50,  -3,   outOfStock,              0,
                           0,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,              0,
                },

//   -----------------------------------------5-7-1对1错一起拿，其他两个对的单独拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        101,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -150,   200,   outOfStock,               2, -100, 100,   outOfStock,         1,
                        -100,    -3,   outOfStock,               0,    0,  -3,   outOfStock,         0,
                           0,    -3,   outOfStock,               0,    0,  -3,   outOfStock,         0,
                },
                new Object[]{
                        102,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -100,   250,   wrongPlaceAndOutOfStock,      2, -150, 100,   outOfStock,     1,
                        -100,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,     0,
                           0,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,     0,
                },
                new Object[]{
                        103,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -100,   250,   wrongPlaceAndOutOfStock,      2, -100, 150,   wrongPlaceAndOutOfStock, 1,
                        -150,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,              0,
                           0,    -3,   outOfStock,                   0,    0,  -3,   outOfStock,              0,
                },

//   -----------------------------------------8-13----------2对的一起拿，其他1对1错单独拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        104,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -50,   300,   "",                            3, -100, 200,   outOfStock,         2,
                        -200,   -3,   outOfStock,                    0,    0,  -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0,    0,  -3,   outOfStock,         0,
                },
                new Object[]{
                        105,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -50,   300,   "",                            3, -200, 100,   outOfStock,         1,
                        -100,   -3,   outOfStock,                    0,    0,  -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0,    0,  -3,   outOfStock,         0,
                },
                new Object[]{
                        106,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -100,   250,   wrongPlaceAndOutOfStock,      2, -50, 200,   outOfStock,         2,
                        -200,    -3,   outOfStock,                   0,   0,  -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,   0,  -3,   outOfStock,         0,
                },
                new Object[]{
                        107,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -100,   250,   wrongPlaceAndOutOfStock,      2, -200, 50,   wrongPlaceAndOutOfStock, 0,
                         -50,    -3,   outOfStock,                   0,    0, -3,   outOfStock,              0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,              0,
                },
                new Object[]{
                        108,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -200,   150,   wrongPlaceAndOutOfStock,      1, -50, 100,   outOfStock,         1,
                        -100,    -3,   outOfStock,                   0,   0,  -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,   0,  -3,   outOfStock,         0,
                },
                new Object[]{
                        109,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -200,   150,   wrongPlaceAndOutOfStock,      1, -100, 50,   wrongPlaceAndOutOfStock, 0,
                         -50,    -3,   outOfStock,                   0,    0, -3,   outOfStock,              0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,              0,
                },

//     ------------------------------14-15----------3个对的一起拿，1错的单独拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        110,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -300,   50,   wrongPlaceAndOutOfStock,       0, -50, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0,   0, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0,   0, -3,   outOfStock,         0,
                },
                new Object[]{
                        111,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -50,   300,   "",                            3, -300, -3,   outOfStock,         0,
                          0,    -3,   outOfStock,                    0,    0, -3,   outOfStock,         0,
                          0,    -3,   outOfStock,                    0,    0, -3,   outOfStock,         0,
                },

//     ------------------------------16-17----------1个对的单独拿，其他一起拿----------------------------------------------------------------------------------------------------------
                new Object[]{
                        112,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -250,   100,   outOfStock,                   1, -100, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,         0,
                },
                new Object[]{
                        113,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -100,   250,   wrongPlaceAndOutOfStock,      2, -250, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                   0,    0, -3,   outOfStock,         0,
                },

//     ------------------------------18----------都一起拿----------------------------------------------------------------------------------------------------------

                new Object[]{
                        114,
                        100, 300, 3, 300,  50, 350, wrongPlace, 3,
                        -350,   -3,   outOfStock,                    0, 0, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0, 0, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                    0, 0, -3,   outOfStock,         0,
                },


// -----------------------------------2个对的，1个错的--------------共8个case----------------------------------------
                new Object[]{
                        115,
                        100, 200, 2, 200,  50, 250, wrongPlaceAndOutOfStock, 2,
                        -50,   200,   outOfStock,                                 2, -200, -3,   outOfStock,  0,
                          0,    -3,   outOfStock,                                 0,    0, -3,   outOfStock,  0,
                          0,    -3,   outOfStock,                                 0,    0, -3,   outOfStock,  0,
                },
                new Object[]{
                        116,
                        100, 200, 2, 200,  50, 250, wrongPlaceAndOutOfStock, 2,
                        -150,   100,   outOfStock,                                1, -100, -3,   outOfStock,  0,
                           0,    -3,   outOfStock,                                0,    0, -3,   outOfStock,  0,
                           0,    -3,   outOfStock,                                0,    0, -3,   outOfStock,  0,
                },
                new Object[]{
                        117,
                        100, 200, 2, 200,  50, 250, wrongPlaceAndOutOfStock, 2,
                        -200,   50,   wrongPlaceAndOutOfStock,                    0, -50, -3,   outOfStock,  0,
                           0,   -3,   outOfStock,                                 0,   0, -3,   outOfStock,  0,
                           0,   -3,   outOfStock,                                 0,   0, -3,   outOfStock,  0,
                },
                new Object[]{
                        118,
                        100, 200, 2, 200,  50, 250, wrongPlaceAndOutOfStock, 2,
                        -100,   150,   wrongPlaceAndOutOfStock,                   1, -150, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                                0,    0, -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                                0,    0, -3,   outOfStock,         0,
                },
                new Object[]{
                        119,
                        100, 200, 2, 200,  50, 250, wrongPlaceAndOutOfStock, 2,
                        -100,   150,   wrongPlaceAndOutOfStock,                   1, -100, 50,   wrongPlaceAndOutOfStock, 0,
                         -50,    -3,   outOfStock,                                0,    0, -3,   outOfStock,              0,
                           0,    -3,   outOfStock,                                0,    0, -3,   outOfStock,              0,
                },
                new Object[]{
                        120,
                        100, 200, 2, 200,  50, 250, wrongPlaceAndOutOfStock, 2,
                        -100,   150,   wrongPlaceAndOutOfStock,                   1, -50, 100,   outOfStock,         1,
                        -100,    -3,   outOfStock,                                0,   0,  -3,   outOfStock,         0,
                           0,    -3,   outOfStock,                                0,   0,  -3,   outOfStock,         0,
                },
                new Object[]{
                        121,
                        100, 200, 2, 200,  50, 250, wrongPlaceAndOutOfStock, 2,
                        -50,   200,   outOfStock,                                 2, -100, 100,   outOfStock,        1,
                        -100,   -3,   outOfStock,                                 0,    0,  -3,   outOfStock,        0,
                           0,   -3,   outOfStock,                                 0,    0,  -3,   outOfStock,        0,
                },
                new Object[]{
                        122,
                        100, 200, 2, 200,  50, 250, wrongPlaceAndOutOfStock, 2,
                        -250,   -3,   outOfStock,                                 0, 0, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                                 0, 0, -3,   outOfStock,         0,
                           0,   -3,   outOfStock,                                 0, 0, -3,   outOfStock,         0,
                },

//---------------------------------------1对的，1错的-----------------------------------------------
                new Object[]{
                        123,
                        100, 100, 1, 100,  50, 150, wrongPlaceAndOutOfStock, 1,
                        -100,   50,   wrongPlaceAndOutOfStock,                    0, -50, -3,   outOfStock,       0,
                           0,   -3,   outOfStock,                                 0,   0, -3,   outOfStock,       0,
                           0,   -3,   outOfStock,                                 0,   0, -3,   outOfStock,       0,
                },
                new Object[]{
                        124,
                        100, 100, 1, 100,  50, 150, wrongPlaceAndOutOfStock, 1,
                        -50,   100,   outOfStock,                                 1, -100, -3,   outOfStock,    0,
                          0,    -3,   outOfStock,                                 0,    0, -3,   outOfStock,    0,
                          0,    -3,   outOfStock,                                 0,    0, -3,   outOfStock,    0,
                },
                new Object[]{
                        125,
                        100, 100, 1, 100,  50, 150, wrongPlaceAndOutOfStock, 1,
                        -150,   -3,   outOfStock,                                 0, 0, -3,   outOfStock,       0,
                           0,   -3,   outOfStock,                                 0, 0, -3,   outOfStock,       0,
                           0,   -3,   outOfStock,                                 0, 0, -3,   outOfStock,       0,
                },


        };
        return objects;
    }
}

