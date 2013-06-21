package rungekutta;

public class MathExpression {
    
    public enum Operation
    {
        PLUS, MINUS, MULT, DIV, POW
    }
    
    public enum Type
    {
        VAR, CONST, FUNC, OPERATION, NONE
    }
    
    public enum Func
    {
        SIN, COS, LN, EXP
    }
    
    public MathExpression left, right;
    public Type t;
    public Operation op;
    public double cons;
    public Func f;
    
    public MathExpression()
    {
        left = null;
        right = null;
    }
    
    private static int prior(char c)
    {
        if(c == '+' || c == '-')
            return 0;
        if(c == '*' || c == '/')
            return 1;
        if(c == '^')
            return 2;
        if(c == 's' || c == 'c' || c == 'l')
            return 3;
        if(c == 't')
            return 4;
        else
            return 4;
    }
        
    private static boolean is_op(char c)
    {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }
    
    private static Type getType(String s, int ind)
    {
        if(is_op(s.charAt(ind)))
            return Type.OPERATION;
        if(s.startsWith("sin", ind) || s.startsWith("cos", ind) || s.startsWith("log", ind))
            return Type.FUNC;
        if(s.charAt(ind) == 't')
            return Type.VAR;
        if((int)s.charAt(ind) >= (int)'0' && (int)s.charAt(ind) <= (int)'9')
            return Type.CONST;
        if(s.charAt(ind) == 'e')
            return Type.CONST;
        if(s.startsWith("pi", ind))
            return Type.CONST;
        else
            return Type.NONE;
    }
    
    private static Func getFunc(String s, int ind)
    {
        if(s.startsWith("sin", ind))
            return Func.SIN;
        if(s.startsWith("cos", ind))
            return Func.COS;
        if(s.startsWith("log", ind))
            return Func.LN;
        else
            throw new IllegalArgumentException("Невозможно распознать функцию");
    }
    
    private static Operation getOperation(String s, int ind)
    {
        if(s.charAt(ind) == '+')
            return Operation.PLUS;
        if(s.charAt(ind) == '-')
            return Operation.MINUS;
        if(s.charAt(ind) == '*')
            return Operation.MULT;
        if(s.charAt(ind) == '/')
            return Operation.DIV;
        if(s.charAt(ind) == '^')
            return Operation.POW;
        else
            throw new IllegalArgumentException("Невозможно распознать функцию");
    }
    
    private static double getConst(String s, int ind)
    {
        if(s.charAt(ind) == 'e')
            return Math.E;
        else if(s.startsWith("pi", ind))
            return Math.PI;
        else
            return Double.parseDouble(s);
    }
    
    public static MathExpression parseString(String s)
    {
        int minBr = Integer.MAX_VALUE;
        MathExpression res = new MathExpression();
        int last = 0;
        while(minBr > 0)
        {
            int brackets = 0;
            last = 0;
            char cur, prev = '(';
            int p = 10;
            for(int i = 0; i < s.length(); i++)
            {
                cur = s.charAt(i);
                if(cur == ' ')
                    continue;
                if(cur == '(')
                    brackets++;
                if(cur == ')')
                    brackets--;
                if((i != s.length() - 1) || (s.length() == 1))
                    minBr = Math.min(minBr, brackets);
                if(brackets == 0)
                {
                    if(p >= prior(cur))
                    {
                        if((prior(cur) == 0 && prev != '(') || (prior(cur) != 0))
                        {
                            if(getType(s, i) != Type.NONE)
                            {
                                p = prior(cur);
                                last = i;
                            }
                        }
                        else
                        {
                            String tmp = "";
                            for(int j = 0; j < i; j++)
                                tmp += s.charAt(j);
                            tmp += '0';
                            for(int j = i; j < s.length(); j++)
                                tmp += s.charAt(j);
                            prev = '0';
                            s = tmp;
                            continue;
                        }
                    }
                }
                prev = cur;
            }
            if(minBr != 0)
            {
                String newS = "";
                for(int i = 1; i < s.length() - 1; i++)
                    newS += s.charAt(i);
                s = newS;
            }
        }
        res.t = getType(s, last);
        if(res.t == Type.FUNC)
        {
            res.f = getFunc(s, last);
            String newS = "";
            for(int i = 3; i < s.length(); i++)
                newS += s.charAt(i);
            res.left = null;
            res.right = parseString(newS);
        }
        else if (res.t == Type.OPERATION)
        {
            res.op = getOperation(s, last);
            String ls = "", rs = "";
            for(int i = 0; i < last; i++)
                ls += s.charAt(i);
            for(int i = last + 1; i < s.length(); i++)
                rs += s.charAt(i);
            res.left = parseString(ls);
            res.right = parseString(rs);
        }
        else if (res.t == Type.CONST)
            res.cons = getConst(s, last);
        return res;
    }
    
    public double getValue(double var)
    {
        if(t == Type.CONST)
            return cons;
        else if(t == Type.VAR)
            return var;
        else if (t == Type.OPERATION)
        {
            if(op == Operation.PLUS)
                return left.getValue(var) + right.getValue(var);
            else if(op == Operation.MINUS)
                return left.getValue(var) - right.getValue(var);
            else if (op == Operation.MULT)
                return left.getValue(var) * right.getValue(var);
            else if (op == Operation.DIV)
                return left.getValue(var) / right.getValue(var);
            else if (op == Operation.POW)
                return Math.pow(left.getValue(var) , right.getValue(var));
        }
        else
        {
            if(f == Func.SIN)
                return Math.sin(right.getValue(var));
            else if(f == Func.COS)
                return Math.cos(right.getValue(var));
            else if(f == Func.LN)
                return Math.log(right.getValue(var));
        }
        throw new IllegalArgumentException("Невозможно распознать функцию");
    }
    
}
