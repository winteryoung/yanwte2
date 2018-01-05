task :publish do
  sh "mvn clean deploy -P release"
end

# 变更版本
task :change_version, [:ver] do |t, args|
  ver = args[:ver]
  sh "mvn versions:set -DnewVersion=#{ver}"
  change_readme_version(ver)
  change_demo_version(ver)
end

def change_readme_version(ver)
    str = nil
  File.open("README.md", "r") do |file|
    str = file.read
    str = str.gsub(/<version>.+?<\/version>/, "<version>#{ver}</version>")
  end

  File.open("README.md", "w") do |file|
    file.write(str)
  end
end

def change_demo_version(ver)
    str = nil
  File.open("yanwte2-demo/pom.xml", "r") do |file|
    str = file.read
    str = str.gsub(/<yanwte2.version>.+?<\/yanwte2.version>/, "<yanwte2.version>#{ver}</yanwte2.version>")
  end

  File.open("yanwte2-demo/pom.xml", "w") do |file|
    file.write(str)
  end
end