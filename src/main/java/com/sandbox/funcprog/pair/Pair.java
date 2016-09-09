package com.sandbox.funcprog.pair;

 

public class Pair<L, R> {

 

    private final L left;

    private final R right;

 

    public static <X, Y> Pair<X, Y> pair(X x, Y y) {

        return new Pair<>(x, y);

    }

 

    private Pair(L left, R right) {

        super();

        this.left = left;

        this.right = right;

    }

 

    public L left() {

        return left;

    }

 

    public R right() {

        return right;

    }

 

    @Override

    public boolean equals(Object obj) {

        if (this == obj)

            return true;

        if (obj == null)

            return false;

        if (getClass() != obj.getClass())

            return false;

        @SuppressWarnings("unchecked")

        Pair<L, R> other = (Pair<L, R>) obj;

        if (left == null) {

            if (other.left != null)

                return false;

        } else if (!left.equals(other.left))

            return false;

        if (right == null) {

            if (other.right != null)

                return false;

        } else if (!right.equals(other.right))

            return false;

        return true;

    }

 

    @Override

    public int hashCode() {

        final int prime = 31;

        int result = 1;

        result = prime * result + ((left == null) ? 0 : left.hashCode());

        result = prime * result + ((right == null) ? 0 : right.hashCode());

        return result;

    }

 

}  