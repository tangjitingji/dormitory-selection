package cn.edu.pku.tangjiting.dormitoryselection.entity;

import java.util.LinkedHashMap;

public  class Building  {
        private int five;
        private int thirteen;
        private int fourteen;
        private int eight;
        private int nine;
        // TODO: 2017/12/23

        public int get5() {
            return five;
        }

        public void set5(int five) {
            this.five = five;
        }

        public int get13() {
            return thirteen;
        }

        public void set13(int thirteen) {
            this.thirteen = thirteen;
        }

        public int get14() {
            return fourteen;
        }

        public void set14(int fourteen) {
            this.fourteen = fourteen;
        }

        public int get8() {
            return eight;
        }

        public void set8(int eight) {
            this.eight = eight;
        }

        public int get9() {
            return nine;
        }

        public void set9(int nine) {
            this.nine = nine;
        }

        public  LinkedHashMap<String,Integer> getAvailBuilding(int needNum){
            LinkedHashMap<String,Integer> availBuildingArr = new LinkedHashMap<String,Integer>();
            if(get5() > needNum){
                availBuildingArr.put("5号楼",5);
            }
            if(get13() > needNum){
                availBuildingArr.put("13号楼",13);
            }
            if(get14() > needNum){
                availBuildingArr.put("14号楼",14);
            }
            if(get8() > needNum){
                availBuildingArr.put("8号楼",8);
            }
            if(get9() > needNum){
                availBuildingArr.put("9号楼",9);
            }
            return availBuildingArr;
        }

        @Override
        public String toString() {
            return "BuildingCollection{" +
                    "five=" + five +
                    ", thirteen=" + thirteen +
                    ", fourteen=" + fourteen +
                    ", eight=" + eight +
                    ", nine=" + nine +
                    '}';
        }
    }

