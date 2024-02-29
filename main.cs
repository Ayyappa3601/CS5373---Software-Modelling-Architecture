using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;

namespace Project
{
    class Program
    {
        static void Main(string[] args)
        {
            // Please provide a path where you want to save the CNF file.
            string cnfFilePath = ".\\Desktop\\cnffile.cnf";
            int n = 0;
            while (true)
            {
                try
                {
                    Console.WriteLine("Please enter n-queen value:");
                    string input = Console.ReadLine();
                    n = Convert.ToInt32(input);
                    if (n < 4)
                    {
                        Console.WriteLine("N-queens problem for the given N is not satisfiable. Please enter a value greater than 3");
                    }
                    else
                    {
                        break;
                    }
                }
                catch (Exception e)
                {
                    Console.WriteLine("Entered number is not an integer input must be a integer.");
                }                
            }            

            int totalPositions = n * n;
            StringBuilder comments = new StringBuilder();
            comments.AppendLine("c There are " + totalPositions + " positions");
            comments.AppendLine("c the clauses for " + n + "-queen are below");

            StringBuilder data = new StringBuilder();
            data = GetRowsData(n, data);
            data = GetColumnsData(n, data);
            data = GetDiagnolsData(n, data);
            var count = Regex.Matches(data.ToString(), Environment.NewLine).Count;            
            comments.AppendLine("p cnf " + Convert.ToString(totalPositions) + " " + Convert.ToString(count)); 
            File.WriteAllText(cnfFilePath, comments.ToString() + data.ToString());
        }

        /// <summary>
        /// Returns clauses for row
        /// </summary>
        private static StringBuilder GetRowsData(int n, StringBuilder data)
        {            
            for (int i = 0; i < n; i++)
            {
                var values = new List<string>();
                for (int j = (i * n) + 1; j < (i * n + (n + 1)); j++)
                {
                    values.Add(Convert.ToString(j));
                    data.Append(j + " ");
                }
                data.Append("0" + Environment.NewLine);
                foreach (var value1 in values)
                {
                    int index = values.IndexOf(value1);
                    foreach (var value2 in values.GetRange(index + 1, values.Count - index - 1))
                    {
                        data.Append("-" + Convert.ToString(value1) + " -" + Convert.ToString(value2) + " 0" + Environment.NewLine);
                    }
                }
            }
            return data;
        }

        /// <summary>
        /// Returns clauses for column
        /// </summary>
        private static StringBuilder GetColumnsData(int n, StringBuilder data)
        {
            for (int i = 0; i < n; i++)
            {
                var values = new List<string>();
                for (int j = i + 1; j < (n * n) + 1; j = j + n)
                {
                    values.Add(Convert.ToString(j));
                    data.Append(j + " ");
                }
                data.Append("0" + Environment.NewLine);
                foreach (var value1 in values)
                {
                    int index = values.IndexOf(value1);
                    foreach (var value2 in values.GetRange(index + 1, values.Count - index - 1))
                    {
                        data.Append("-" + Convert.ToString(value1) + " -" + Convert.ToString(value2) + " 0" + Environment.NewLine);
                    }
                }
            }
            return data;
        }

        /// <summary>
        /// Returns clauses of diagonals in the lower triangle from left to right
        /// </summary>
        private static StringBuilder GetDiagnolsData(int n, StringBuilder data)
        {
            data = GetBottomTriangleFromLeftToRight(n, data);            
            data = GetUpperTriangleFromLeftToRight(n, data);
            
            data = GetBottomTriangleFromRightToLeft(n, data);
            data = GetUpperTriangleFromRightToLeft(n, data);
            return data;
        }

        /// <summary>
        /// Returns clauses of diagonals in the lower triangle from left to right
        /// </summary>        
        private static StringBuilder GetBottomTriangleFromLeftToRight(int n, StringBuilder data)
        {
            for (int i = n - 1; i > -1; i--) // loop through each row
            {
                var values = new List<string>();
                for (int j = 0; j < n - i; j++)
                {
                    int position = ((i + j) * n) + j + 1;
                    values.Add(Convert.ToString(position));
                }
                data.Append(GetDiagonalClauses(values));
            }
            return data;
        }

        /// <summary>
        /// Returns clauses of diagonals in the upper triangle from left to right
        /// </summary>        
        private static StringBuilder GetUpperTriangleFromLeftToRight(int n, StringBuilder data)
        {
            for (int i = 1; i < n; i++) // loop through each column
            {
                var values = new List<string>();
                for (int j = 0; j < n - i; j++)
                {
                    int position = (j * n) + i + j + 1;
                    values.Add(Convert.ToString(position));
                }
                data.Append(GetDiagonalClauses(values));
            }
            return data;
        }

        /// <summary>
        /// Returns clauses of diagonals in the bottom triangle from right to left
        /// </summary>        
        private static StringBuilder GetBottomTriangleFromRightToLeft(int n, StringBuilder data)
        {
            for (int i = n - 1; i > -1; i--) // loop through each row
            {
                var values = new List<string>();
                for (int j = 0; j < n - i; j++)
                {
                    int position = ((i + j) * n) + n - 1 - j + 1;
                    values.Add(Convert.ToString(position));
                }
                data.Append(GetDiagonalClauses(values));
            }
            return data;
        }

        /// <summary>
        /// Returns clauses of diagonals  in the upper triangle from right to left
        /// </summary>        
        private static StringBuilder GetUpperTriangleFromRightToLeft(int n, StringBuilder data)
        {
            for (int i = n - 2; i > -1; i--) // loop through each column
            {
                var values = new List<string>();
                for (int j = 0; j < i + 1; j++)
                {
                    int position = (j * n) + i - j + 1;
                    values.Add(Convert.ToString(position));
                }
                data.Append(GetDiagonalClauses(values));
            }
            return data;
        }

        private static string GetDiagonalClauses(List<string> values)
        {
            string clause = "";
            foreach (var value1 in values)
            {
                int index = values.IndexOf(value1);
                foreach (var value2 in values.GetRange(index + 1, values.Count - index - 1))
                {
                    clause += "-" + Convert.ToString(value1) + " -" + Convert.ToString(value2) + " 0" + Environment.NewLine;
                }
            }
            return clause;
        }
    }
}
