package com.pur.utils;


import com.pur.model.Relation;

import java.util.List;

/**
 * 位置调整工具类
 */
public class GraphUtil
{

    private static final int SPACE = 20;

    public static void createRelation(List<Relation> relations)
    {

        Relation root;
        if (relations.size() == 1) {
            root = relations.get(0);
        } else {
            //如果有多个源的话，需要创建一个虚拟的根节点
            root = createRoot(relations);
        }
        root.setX(-143);
        root.setY(-470);
        calLocation(root);
    }


    /**
     * 创建虚root节点
     */
    private static Relation createRoot(List<Relation> relations)
    {
        Relation root = new Relation();
        root.setVirtual(true);
        root.setTargets(relations);
        root.setWidth(0);
        root.setHeight(0);
        return root;
    }

    /**
     * 计算位置
     */
    private static void calLocation(Relation parent)
    {
        List<Relation> targets = parent.getTargets();
        if (!targets.isEmpty()) {
            int size = targets.size();
            calChildrenLocation(parent, size % 2 != 0);
            for (Relation target : targets) {
                calLocation(target);
            }
        }
    }

    private static void calChildrenLocation(Relation parent, boolean odd)
    {
        List<Relation> targets = parent.getTargets();
        int size = targets.size();
        int middle = odd ? (size - 1) / 2 : size / 2;

        int x = parent.getX();
        int y = parent.getY();

        int locX = x;
        int locY = y + 250;

        int rightIndex = middle;
        Relation last = null;
        Relation middleElement = null;

        if (odd) {
            //中间元素
            middleElement = targets.get(middle);
            middleElement.setX(x);
            middleElement.setY(locY);
            last = middleElement;
            rightIndex = middle + 1;
        } else {
            if (size > 1) {
                //计算左边最里面第一个child的宽度
                Relation leftChild = targets.get(middle - 1);
                int leftWidth = Math.abs(getNodeWidth(leftChild));
                locX -= (leftWidth - leftChild.getWidth()) / 2;
            }
        }

        //左边
        for (int i = middle - 1; i >= 0; i--) {
            Relation target = targets.get(i);
            if (last != null) {
                int lastNodeWidth = Math.abs(getNodeWidth(last));
                int width = Math.abs(getNodeWidth(target));
                int leftX = locX - (lastNodeWidth - last.getWidth()) / 2 - SPACE - (width + target.getWidth()) / 2;
                target.setX(leftX);
            } else {
                target.setX(locX - SPACE - parent.getWidth());
            }
            target.setY(locY);
            last = target;
            locX = target.getX();
        }

        //重置locX
        locX = x;
        //右边
        if (odd) {
            last = middleElement;
        } else {
            last = null;
            if (size > 1) {
                //计算右边最里面第一个child的宽度
                Relation rightChild = targets.get(rightIndex);
                int rightWidth = Math.abs(getNodeWidth(rightChild));
                locX += (rightWidth - rightChild.getWidth()) / 2;
            }
        }

        for (int i = rightIndex; i < size; i++) {
            Relation target = targets.get(i);
            if (last != null) {
                int lastNodeWidth = Math.abs(getNodeWidth(last));
                int width = Math.abs(getNodeWidth(target));
                int leftX = locX + (lastNodeWidth + last.getWidth()) / 2 + SPACE + (width - target.getWidth()) / 2;
                target.setX(leftX);
            } else {
                target.setX(locX + parent.getWidth() + SPACE);
            }
            target.setY(locY);
            last = target;
            locX = target.getX();
        }
    }

    /**
     * 计算整个节点(包括子节点)的宽度
     */
    private static int getNodeWidth(Relation node)
    {
        int leaves = getLeafElements(node);
        if (leaves == 0) {
            return node.getWidth();
        }
        if (leaves % 2 == 0) {
            //偶数节点要按奇数个来算宽度
            leaves++;
        }
        return leaves * 230 + (leaves - 1) * SPACE;
    }

    private static int getLeafElements(Relation node)
    {
        int count = 0;
        List<Relation> targets = node.getTargets();
        for (Relation target : targets) {
            if (target.getTargets().isEmpty()) {
                count++;
            } else {
                count += getLeafElements(target);
            }
        }
        return count;
    }
}
